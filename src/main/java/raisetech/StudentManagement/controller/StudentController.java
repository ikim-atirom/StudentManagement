package raisetech.StudentManagement.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;
import raisetech.StudentManagement.service.StudentService;

/**
 * 受講生情報を管理（一覧検索、詳細情報表示、新規登録、更新）するコントローラーです。
 */
@Validated
@RestController
public class StudentController { // UI層

  private StudentService service;
  private StudentRepository repository;

  @Autowired
  public StudentController(StudentService service, StudentRepository repository) {
    this.service = service;
    this.repository = repository;
  }

  /**
   * 受講生詳細の一覧を全件検索します。
   *
   * @return 受講生詳細一覧検索
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudents();
  }

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  @GetMapping("/studentInfo/{studentId}")
  public StudentDetail getStudentInfo(@PathVariable("studentId") Integer studentId) {
    return service.getStudentInfo(studentId);
  }

  /**
   * アクティブな受講生の一覧を検索します。
   *
   * @return アクティブな受講生一覧
   */
  @GetMapping("/activeStudentList")
  public List<Student> getActiveStudentList() {
    return service.searchActiveStudents();
  }

  /**
   * 受講生のコース情報を全件検索します。
   *
   * @return 受講生コース情報一覧
   */
  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.searchStudentCourses();
  }

  /**
   * 受講生詳細情報を新規登録します。
   *
   * @param studentDetail 受講生詳細情報
   * @return 登録された受講生詳細情報
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細情報を更新、削除（論理削除）します。
   *
   * @param studentDetail 更新する受講生詳細情報
   * @return 更新処理の結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudentDetail(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
