package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
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
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudent();
    List<StudentCourse> studentsCoursesList = repository.searchCourse();
    return converter.convertStudentDetails(studentList, studentsCoursesList);
  }

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  public StudentDetail getStudentInfo(int studentId) {
    Student student = repository.findStudentByStudentId(studentId);
    List<String> selectedCourses = repository.findSelectedCoursesByStudentId(student.getStudentId());
    return StudentDetail.forUpdate(student, selectedCourses);
  }

  /**
   * アクティブな受講生の一覧を検索します。
   *
   * @return アクティブな受講生一覧
   */
  public List<Student> searchActiveStudentList() {
    return repository.searchAllActiveStudents();
  }

  /**
   * 受講生のコース情報を全件検索します。
   *
   * @return 受講生コース情報一覧
   */
  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchCourse();
  }

  /**
   * 受講生を新規登録します。
   *
   * @param studentDetail 受講生の詳細情報
   * @return 登録された受講生の詳細情報
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = new Student();
    student.setFullName(studentDetail.getStudent().getFullName());
    student.setKanaName(studentDetail.getStudent().getKanaName());
    student.setNickname(studentDetail.getStudent().getNickname());
    student.setAge(studentDetail.getStudent().getAge());
    student.setGender(studentDetail.getStudent().getGender());
    student.setEmail(studentDetail.getStudent().getEmail());
    student.setAddress(studentDetail.getStudent().getAddress());
    repository.registerStudent(student);
    // MySQLのカウントアップで自動生成した受講生IDを反映
    studentDetail.getStudent().setStudentId(student.getStudentId());
    registerStudentCourses(student.getStudentId(), studentDetail.getSelectedCourseNames());
    studentDetail.setStudentsCourses(repository.findCourseOfSelectedStudent(student.getStudentId()));
    return studentDetail;
  }

  /**
   * 受講生のコース情報を登録します。
   *
   * @param studentId 受講生ID
   * @param selectedCourseNames 選択されたコース名のリスト
   */
  public void registerStudentCourses(Integer studentId, List<String> selectedCourseNames) {
    // 受講開始日を登録時間に、終了予定日を1年後に設定
    LocalDate startDate = LocalDate.now();
    LocalDate endDate = startDate.plusYears(1);

    // 入力フォームで受け取ったコース情報に、受講生ID、受講開始日、終了予定日を紐づけ
    for (String courseName : selectedCourseNames) {
      StudentCourse studentCourse = new StudentCourse();
      studentCourse.setStudentId(studentId);
      studentCourse.setCourseName(courseName);
      studentCourse.setStartDate(startDate);
      studentCourse.setEndDate(endDate);
      repository.registerStudentCourse(studentCourse);
    }
  }

  /**
   * 受講生情報、受講生コース情報を更新します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   */
  @Transactional
  public void updateStudentDetail(StudentDetail studentDetail) {
    updateStudent(studentDetail);
    addStudentCourse(studentDetail);
    deleteStudentCourse(studentDetail);
  }

  /**
   * 受講生の情報を更新します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }

  /**
   * 受講生のコース情報を更新（追加）します。
   *
   * @param studentDetail 更新する受講生の詳細情報
   */
  @Transactional
  public void addStudentCourse(StudentDetail studentDetail) {
    Integer studentId = studentDetail.getStudent().getStudentId(); // 使用頻度高くなりそうなら共通化
    List<String> existingCourseNames = getExistingCourseNames(studentId);
    List<String> updateStudentCourseNames = getUpdatedCourseNames(studentDetail);
    LocalDate startDate = LocalDate.now(); // これも共通化してもよかったかも
    LocalDate endDate = startDate.plusYears(1);
    // 追加が必要なコース（コース名）
    List<String> coursesToAdd = new ArrayList<>();
    for (String courseName : updateStudentCourseNames) {
      if (!existingCourseNames.contains(courseName)) {
        coursesToAdd.add(courseName);
      }
    }
    for (String courseName : coursesToAdd) {
      StudentCourse newCourse = new StudentCourse();
      newCourse.setStudentId(studentId);
      newCourse.setCourseName(courseName);
      newCourse.setStartDate(startDate);
      newCourse.setEndDate(endDate);
      repository.registerStudentCourse(newCourse);
    }
  }

  /**
   * 受講生のコース情報を更新（削除）します。
   */
  @Transactional
  public void deleteStudentCourse(StudentDetail studentDetail) {
    Integer studentId = studentDetail.getStudent().getStudentId();
    List<StudentCourse> existingCourses = getExistingCourses(studentId);
    List<String> updateStudentCourseNames = getUpdatedCourseNames(studentDetail);
    // 削除が必要なコース
    List<StudentCourse> coursesToRemove = new ArrayList<>();
    for (StudentCourse course : existingCourses) {
      if (!updateStudentCourseNames.contains(course.getCourseName())) {
        coursesToRemove.add(course);
      }
    }
    for (StudentCourse course : coursesToRemove) {
      repository.deleteStudentCourseByCourseId(course.getCourseId());
    }
  }

  private List<StudentCourse> getExistingCourses(Integer studentId) {
    return repository.findCourseOfSelectedStudent(studentId);
  }

  private List<String> getExistingCourseNames(Integer studentId) {
    List<StudentCourse> existingCourses = getExistingCourses(studentId);
    List<String> existingCourseNames = new ArrayList<>();
    for (StudentCourse course : existingCourses) {
      existingCourseNames.add(course.getCourseName());
    }
    return existingCourseNames;
  }

  private List<String> getUpdatedCourseNames(StudentDetail studentDetail) {
    return studentDetail.getSelectedCourseNames();
  }
}
