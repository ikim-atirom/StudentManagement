package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFormattedTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudent();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchCourse();
  }

  // 受講生登録
  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    Student student = new Student();
    student.setFullName(studentDetail.getStudent().getFullName());
    student.setKanaName(studentDetail.getStudent().getKanaName());
    student.setNickname(studentDetail.getStudent().getNickname());
    student.setAge(studentDetail.getStudent().getAge());
    student.setGender(studentDetail.getStudent().getGender());
    student.setEmail(studentDetail.getStudent().getEmail());
    student.setAddress(studentDetail.getStudent().getAddress());
    repository.insertStudent(student);
    // MySQLのカウントアップで自動生成した受講生IDを反映
    studentDetail.getStudent().setStudentId(student.getStudentId());
  }

  // 受講生コース登録
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
      repository.insertStudentCourse(studentCourse);
    }
  }

  // 選択した受講生の情報を表示
  public Student searchStudentByStudentId(int studentId) {
    return repository.findStudentByStudentId(studentId);
  }

  // 選択した受講生のコース情報を取得
  public List<String> getSelectedCoursesByStudentId(int studentId) {
    return repository.findSelectedCoursesByStudentId(studentId);
  }

  // 受講生情報の更新
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
  }

  // privateで共通化したメソッド上に移動しようか
  // 既存のコース情報の取得
  private List<StudentCourse> getExistingCourses(Integer studentId) {
    return repository.findCourseOfSelectedStudent(studentId);
  }
  // 既存のコース情報（コース名）の取得
  private List<String> getExistingCourseNames(Integer studentId) {
    List<StudentCourse> existingCourses = getExistingCourses(studentId);
    List<String> existingCourseNames = new ArrayList<>();
    for (StudentCourse course : existingCourses) { // 見慣れたらStreamAPI使う
      existingCourseNames.add(course.getCourseName());
    }
    return existingCourseNames;
  }
  // htmlで受け取ったコース情報（コース名）
  private List<String> getUpdatedCourseNames(StudentDetail studentDetail) {
    return studentDetail.getSelectedCourseNames();
  }

  // 受講生コース情報の更新（追加）
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
      repository.insertStudentCourse(newCourse);
    }
  }

  // 受講生コース情報の更新（削除）
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
}
