package raisetech.StudentManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.exception.StudentNotFoundException;
import raisetech.StudentManagement.service.StudentService;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  private StudentService service;

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void 受講生詳細の一覧検索が実行され空のリストが返されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/studentList"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[]"));

    verify(service, times(1)).searchStudents();
  }

  @Test
  void IDに紐づく受講生の詳細情報が存在しない場合404エラーが返されること() throws Exception {
    int studentId = 999;

    // サービスのモックを設定
    when(service.getStudentInfo(studentId)).thenThrow(new StudentNotFoundException("受講生が見つかりませんでした。"));

    // モックMVCでIDに紐づく受講生の詳細情報のGETリクエストを実行し、404エラーを検証
    mockMvc.perform(MockMvcRequestBuilders.get("/studentInfo/{studentId}", studentId))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(jsonPath("$.message").value("受講生が見つかりませんでした。"));

    verify(service, times(1)).getStudentInfo(studentId);
  }

  @Test
  void IDに紐づく受講生の詳細情報が取得されること() throws Exception {
    // テスト用の受講生IDと受講生情報を設定
    int studentId = 999;
    Student student = new Student();
    student.setStudentId(999);
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setNickname("テストニックネーム");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");
    // テスト用の受講生コース情報を作成
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("テストコース");
    // テストデータから受講生詳細情報を作成
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setSelectedCourseNames(List.of(studentCourse.getCourseName()));

    // サービスのモックを設定
    when(service.getStudentInfo(studentId)).thenReturn(studentDetail);

    // モックMVCでIDに紐づく受講生の詳細情報のGETリクエストを実行し、期待されるレスポンスを検証
    mockMvc.perform(MockMvcRequestBuilders.get("/studentInfo/{studentId}", studentId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
          """
          {
            "student": {
              "studentId": 999,
             "fullName": "テスト名前",
             "kanaName": "テストナマエ",
             "nickname": "テストニックネーム",
             "age": 0,
             "gender": "回答しない",
             "email": "test@example.com",
             "address": "テスト地域"
            },
            "selectedCourseNames": ["テストコース"]
          }
          """
        ));

    verify(service, times(1)).getStudentInfo(studentId);
  }

  @Test
  void アクティブな受講生の一覧検索が実行されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/activeStudentList"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(service, times(1)).searchActiveStudents();
  }

  @Test
  void 受講生のコース情報の全件検索が実行されること() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/studentCourseList"))
        .andExpect(MockMvcResultMatchers.status().isOk());

    verify(service, times(1)).searchStudentCourses();
  }

  @Test
  void 受講生詳細情報の新規登録が実行されること() throws Exception {
    // テスト用の受講生情報を設定
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setNickname("テストニックネーム");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");
    // テスト用の受講生コース情報を作成
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("テストコース");
    // テストデータから受講生詳細情報を作成
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setSelectedCourseNames(List.of(studentCourse.getCourseName()));

    // サービスのモックを設定
    when(service.registerStudent(any(StudentDetail.class))).thenReturn(studentDetail);

    // モックMVCで受講生詳細情報の新規登録のPOSTリクエストを実行し、期待されるレスポンスを検証
    mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
            .contentType("application/json")
            .content("""
                {
                  "student": {
                    "fullName": "テスト名前",
                    "kanaName": "テストナマエ",
                    "nickname": "テストニックネーム",
                    "age": 0,
                    "gender": "回答しない",
                    "email": "test@example.com",
                    "address": "テスト地域"
                  },
                  "selectedCourseNames": ["テストコース"]
                }
                """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
            """
            {
              "student": {
                "fullName": "テスト名前",
                "kanaName": "テストナマエ",
                "nickname": "テストニックネーム",
                "age": 0,
                "gender": "回答しない",
                "email": "test@example.com",
                "address": "テスト地域"
              },
              "selectedCourseNames": ["テストコース"]
            }
            """
        ));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));
  }

  @Test
  void 受講生詳細情報の更新処理が実行されること() throws Exception {
    // モックMVCで受講生詳細情報の更新のPUTリクエストを実行し、期待されるレスポンスを検証
    mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
            .contentType("application/json")
            .content("""
                {
                  "student": {
                    "studentId": 999,
                    "fullName": "アップデート名前",
                    "kanaName": "アップデートナマエ",
                    "nickname": "アップデートニックネーム",
                    "age": 100,
                    "gender": "回答しない",
                    "email": "update@example.com",
                    "address": "アップデート地域"
                  },
                  "selectedCourseNames": ["アップデートコース"]
                }
                """))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudentDetail(any(StudentDetail.class));
  }

  // バリデーションチェックが適切に行われることを確認するテスト
    @Test
  void 受講生情報の各項目に正しい値が入力されたときエラーが発生しないこと() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(0, violations.size());
  }

  @Test
  void 受講生情報の氏名が空欄のときチェックが適切に行われること() {
    Student student = new Student();
    student.setFullName("");
    student.setKanaName("テストナマエ");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("氏名を入力してください。", violations.iterator().next().getMessage());
  }

  @Test
  void 受講生情報のカナ氏名への入力値が全角カタカナでないときチェックが適切に行われること() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("てすとなまえ");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("全角カタカナで入力してください。", violations.iterator().next().getMessage());
  }

  @Test
  void 受講生情報の年齢が0以上でないときチェックが適切に行われること() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setAge(-1);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("年齢は0以上で入力してください。", violations.iterator().next().getMessage());
  }

  @Test
  void 受講生情報の性別への入力値が選択肢にないときチェックが適切に行われること() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setAge(0);
    student.setGender("無効な性別");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("性別は「男性」「女性」「回答しない」のいずれかで入力してください。", violations.iterator().next().getMessage());
  }

  @Test
  void 受講生情報のメールアドレスへの入力値が正しい形式でないときチェックが適切に行われること() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("テスト@example.com");
    student.setAddress("テスト地域");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("メールアドレスは半角英数字で入力してください。", violations.iterator().next().getMessage());
  }

  @Test
  void 受講生情報の地域が空欄のときチェックが適切に行われること() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("地域を入力してください。", violations.iterator().next().getMessage());
    }

  @Test
  void 受講生コース情報のコース名が空欄のときチェックが適切に行われること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("");

    Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);

    Assertions.assertEquals(1, violations.size());
    Assertions.assertEquals("コース名を入力してください。", violations.iterator().next().getMessage());
  }
}
