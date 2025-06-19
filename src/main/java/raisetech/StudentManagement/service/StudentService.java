package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.StudentNotFoundException;
import raisetech.StudentManagement.repository.StudentRepository;

/**
 * 受講生情報を管理（一覧検索、詳細情報表示、新規登録、更新）するサービスです。
 * 受講生の登録やコースの登録・更新・削除などの処理を行います。
 */
@Service
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter) {
    this.repository = repository;
    this.converter = converter;
  }

  /**
   * 受講生の一覧を全件検索します。
   *
   * @return 受講生一覧
   */
  public List<StudentDetail> searchStudents() {
    List<Student> student = repository.searchStudent();
    List<StudentCourse> studentCourses = repository.searchCourse();
    return converter.convertStudentDetails(student, studentCourses);
  }

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  public StudentDetail getStudentInfo(int studentId) {
    Student student = repository.findStudentByStudentId(studentId);
    if (student == null) {
      throw new StudentNotFoundException("受講生ID" + studentId + "の情報は存在しません。");
    }
    List<String> selectedCourses = repository.findSelectedCoursesByStudentId(student.getStudentId());
    return StudentDetail.forUpdate(student, selectedCourses);
    // 処理をConverterに移してもいいかもしれない
  }

  /**
   * アクティブな受講生の一覧を検索します。
   *
   * @return アクティブな受講生一覧
   */
  public List<Student> searchActiveStudents() {
    return repository.searchAllActiveStudents();
  }

  /**
   * 受講生のコース情報を全件検索します。
   *
   * @return 受講生コース情報一覧
   */
  public List<StudentCourse> searchStudentCourses() {
    return repository.searchCourse();
  }

  /**
   * 受講生を新規登録します。
   *
   * @param studentDetail 受講生詳細情報
   * @return 登録された受講生詳細情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = new Student();

    initStudent(studentDetail, student);
    repository.registerStudent(student);
    // MySQLのカウントアップで自動生成した受講生IDを反映
    studentDetail.getStudent().setStudentId(student.getStudentId());
    registerStudentCourses(student.getStudentId(), studentDetail.getSelectedCourseNames());
    studentDetail.setRegisteredCourses(repository.findCourseOfSelectedStudent(student.getStudentId()));
    return studentDetail;
  }

  /**
   * 受講生の初期情報を登録します。
   *
   * @param studentDetail 受講生詳細情報
   * @param student 新規登録した受講生詳細情報
   */
  private void initStudent(StudentDetail studentDetail, Student student) {
    student.setFullName(studentDetail.getStudent().getFullName());
    student.setKanaName(studentDetail.getStudent().getKanaName());
    student.setNickname(studentDetail.getStudent().getNickname());
    student.setAge(studentDetail.getStudent().getAge());
    student.setGender(studentDetail.getStudent().getGender());
    student.setEmail(studentDetail.getStudent().getEmail());
    student.setAddress(studentDetail.getStudent().getAddress());
  }

  /**
   * 受講生のコース情報を新規登録します。
   *
   * @param studentId 受講生ID
   * @param registeredCourses 登録された受講生コース情報
   */
  public void registerStudentCourses(Integer studentId, List<String> registeredCourses) {
    // 受講開始日を登録時間に、終了予定日を1年後に設定
    LocalDate startDate = getStartDate();
    LocalDate endDate = startDate.plusYears(1);

    // 入力フォームで受け取ったコース情報に、受講生ID、受講開始日、終了予定日を紐づけ
    registeredCourses.forEach(courseName -> {
      StudentCourse studentCourse = initStudentCourse(studentId,
          courseName, startDate, endDate);
      repository.registerStudentCourse(studentCourse);
    });
  }

  /**
   * 受講生コース情報を登録します。
   *
   * @param studentId 受講生ID
   * @param courseName コース名
   * @param startDate 受講開始日
   * @param endDate 終了予定日
   * @return 登録された受講生コース情報
   */
  private StudentCourse initStudentCourse(Integer studentId, String courseName,
      LocalDate startDate, LocalDate endDate) {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(studentId);
    studentCourse.setCourseName(courseName);
    studentCourse.setStartDate(startDate);
    studentCourse.setEndDate(endDate);
    return studentCourse;
  }

  /**
   * 受講生情報、受講生コース情報を更新します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   */
  public void updateStudentDetail(StudentDetail studentDetail) {
    updateStudent(studentDetail);
    addStudentCourse(studentDetail);
    deleteStudentCourse(studentDetail);
  }

  /**
   * 受講生詳細情報を更新します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   */
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }

  /**
   * 受講生のコース情報を更新（追加）します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   */
  public void addStudentCourse(StudentDetail studentDetail) {
    Integer studentId = studentDetail.getStudent().getStudentId();
    List<String> existingCourseList = getExistingCourseList(studentId);
    List<String> updateStudentCourseList = getUpdatedCourseList(studentDetail);
    LocalDate startDate = getStartDate();
    LocalDate endDate = startDate.plusYears(1);
    // 追加が必要なコースを抽出、追加処理
    List<String> coursesToAdd = updateStudentCourseList.stream()
        .filter(courseName -> !existingCourseList.contains(courseName))
        .toList();
    coursesToAdd.stream().map(courseName -> initStudentCourse(studentId, courseName,
            startDate, endDate))
        .forEachOrdered(newCourse -> repository.registerStudentCourse(newCourse));
  }

  /**
   * 受講生のコース情報を更新（削除）します。
   */
  public void deleteStudentCourse(StudentDetail studentDetail) {
    Integer studentId = studentDetail.getStudent().getStudentId();
    List<StudentCourse> existingCourseList = getExistingCourses(studentId);
    List<String> updateStudentCourseNames = getUpdatedCourseList(studentDetail);
    // 削除が必要なコースを抽出、削除処理
    List<StudentCourse> coursesToRemove = existingCourseList.stream()
        .filter(course -> !updateStudentCourseNames.contains(course.getCourseName()))
        .toList();
    coursesToRemove.forEach(
        course -> repository.deleteStudentCourseByCourseId(course.getCourseId()));
  }

  private List<StudentCourse> getExistingCourses(Integer studentId) {
    return repository.findCourseOfSelectedStudent(studentId);
  }

  private List<String> getExistingCourseList(Integer studentId) {
    List<StudentCourse> existingCourses = getExistingCourses(studentId);
    List<String> existingCourseNames = new ArrayList<>();
    for (StudentCourse course : existingCourses) {
      existingCourseNames.add(course.getCourseName());
    }
    return existingCourseNames;
  }

  private List<String> getUpdatedCourseList(StudentDetail studentDetail) {
    return studentDetail.getSelectedCourseNames();
  }

  private LocalDate getStartDate() {
    return LocalDate.now();
  }
}
