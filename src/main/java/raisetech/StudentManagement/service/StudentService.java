package raisetech.StudentManagement.service;

import java.time.LocalDate;
import java.util.List;
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
    return repository.searchStudentByStudentId(studentId);
  }

  // 選択した受講生のコース情報を取得
  public List<String> getSelectedCoursesByStudentId(int studentId) {
    return repository.getSelectedCoursesByStudentId(studentId);
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    int updateRows = repository.updateStudent(student);
    if (updateRows == 0) {
      throw new RuntimeException("Student update failed fot student_id: " + student.getStudentId());
    }

    repository.updateStudent(student);
  }
}
