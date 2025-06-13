package raisetech.StudentManagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  @Operation(
      summary = "一覧検索",
      description = "受講生詳細の一覧を全件検索します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "受講生詳細一覧の取得に成功しました。"),
          @ApiResponse(responseCode = "500", description = "サーバーエラーが発生しました。")
      }
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudents();
  }

  @Operation(
      summary = "受講生詳細情報の取得",
      description = "IDに紐づく受講生の詳細情報を取得します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "受講生詳細情報の取得に成功しました。"),
          @ApiResponse(responseCode = "404", description = "受講生が見つかりませんでした。"),
          @ApiResponse(responseCode = "500", description = "サーバーエラーが発生しました。")
      }
  )
  @GetMapping("/studentInfo/{studentId}")
  public StudentDetail getStudentInfo(
      @Parameter(description = "受講生ID")
      @PathVariable("studentId") Integer studentId) {
    return service.getStudentInfo(studentId);
  }

  @Operation(
      summary = "アクティブな受講生の一覧検索",
      description = "アクティブな受講生の一覧を検索します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "アクティブな受講生の一覧取得に成功しました。"),
          @ApiResponse(responseCode = "500", description = "サーバーエラーが発生しました。")
      }
  )
  @GetMapping("/activeStudentList")
  public List<Student> getActiveStudentList() {
    return service.searchActiveStudents();
  }

  @Operation(
      summary = "受講生コース情報の全件検索",
      description = "受講生のコース情報を全件検索します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "受講生コース情報の取得に成功しました。"),
          @ApiResponse(responseCode = "500", description = "サーバーエラーが発生しました。")
      }
  )
  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.searchStudentCourses();
  }

  @Operation(
      summary = "受講生の新規登録",
      description = "受講生詳細情報を新規登録します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "受講生の新規登録に成功しました。"),
          @ApiResponse(responseCode = "500", description = "サーバーエラーが発生しました。")
      }
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  @Operation(
      summary = "受講生の更新",
      description = "受講生詳細情報を更新、削除（論理削除）します。",
      responses = {
          @ApiResponse(responseCode = "200", description = "受講生の更新処理が成功しました。"),
          @ApiResponse(responseCode = "404", description = "受講生が見つかりませんでした。"),
          @ApiResponse(responseCode = "500", description = "サーバーエラーが発生しました。")
      }
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudentDetail(
      @RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudentDetail(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
