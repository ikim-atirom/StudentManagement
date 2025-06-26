package raisetech.StudentManagement.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生のコース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchCourse();
    assertThat(actual.size()).isEqualTo(7);
  }

  @Test
  void IDに紐づく受講生のコース情報を取得できること() {
    List<StudentCourse> actual = sut.findCourseOfSelectedStudent(1);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void アクティブな受講生の全件検索が行えること() {
    List<Student> actual = sut.searchAllActiveStudents();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生の新規登録が行えること() {
    Student student = new Student();
    student.setFullName("テスト名前");
    student.setKanaName("テストナマエ");
    student.setNickname("テストニックネーム");
    student.setAge(0);
    student.setGender("回答しない");
    student.setEmail("test@example.com");
    student.setAddress("テスト地域");
    student.setRemark("");
    student.setDeleted(false);

    int beforeCount = sut.searchStudent().size();
    sut.registerStudent(student);

    List<Student> actual = sut.searchStudent();

    assertThat(actual.size()).isEqualTo(beforeCount + 1);
  }

  @Test
  void 受講生コースの登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setStudentId(999);
    studentCourse.setCourseName("テストコース");
    studentCourse.setStartDate(LocalDate.now());
    studentCourse.setEndDate(LocalDate.now().plusYears(1));

    int beforeCount = sut.searchCourse().size();
    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchCourse();

    assertThat(actual.size()).isEqualTo(beforeCount + 1);
  }

  @Test
  void IDに紐づく受講生の情報を取得できること() {
    Student actual = sut.findStudentByStudentId(1);
    assertAll(
        () -> assertThat(actual.getStudentId()).isEqualTo(1),
        () -> assertThat(actual.getFullName()).isEqualTo("モンキー・D・ルフィ"),
        () -> assertThat(actual.getKanaName()).isEqualTo("モンキーディールフィ"),
        () -> assertThat(actual.getNickname()).isEqualTo("ニカ"),
        () -> assertThat(actual.getAge()).isEqualTo(19),
        () -> assertThat(actual.getGender()).isEqualTo("男性"),
        () -> assertThat(actual.getEmail()).isEqualTo("rubber@example.com"),
        () -> assertThat(actual.getAddress()).isEqualTo("フーシャ村"),
        () -> assertThat(actual.getRemark()).isNull(),
        () -> assertThat(actual.isDeleted()).isFalse());
  }

  @Test
  void 受講生が選択したコース名を取得できること() {
    List<String> actual = sut.findSelectedCoursesByStudentId(1);
    assertThat(actual.size()).isEqualTo(2);
    assertThat(actual).contains("AWSフル", "転職クエスト");
  }

  @Test
  void 受講生の情報を更新できること() {
    Student student = new Student();
    student.setStudentId(1);
    student.setFullName("モンキー・D・ルフィ");
    student.setKanaName("モンキーディールフィ");
    student.setNickname("ドンドッタッタ"); // ニックネームを更新
    student.setAge(19);
    student.setGender("男性");
    student.setEmail("rubber@example.com");
    student.setAddress("フーシャ村");
    student.setRemark(null);
    student.setDeleted(false);

    sut.updateStudent(student);
    Student updatedStudent = sut.findStudentByStudentId(1);

    assertAll(
        () -> assertThat(updatedStudent.getFullName()).isEqualTo("モンキー・D・ルフィ"),
        () -> assertThat(updatedStudent.getKanaName()).isEqualTo("モンキーディールフィ"),
        () -> assertThat(updatedStudent.getNickname()).isEqualTo("ドンドッタッタ"),
        () -> assertThat(updatedStudent.getAge()).isEqualTo(19),
        () -> assertThat(updatedStudent.getGender()).isEqualTo("男性"),
        () -> assertThat(updatedStudent.getEmail()).isEqualTo("rubber@example.com"),
        () -> assertThat(updatedStudent.getAddress()).isEqualTo("フーシャ村"),
        () -> assertThat(updatedStudent.getRemark()).isNull(),
        () -> assertThat(updatedStudent.isDeleted()).isFalse()
    );
  }

  @Test
  void IDに紐づく受講生のコース情報を削除できること() {
    sut.deleteStudentCourseByCourseId(7);
    // コースIDが7に紐づく受講生（2コース受講登録中）のコース情報が削除されたことを確認
    List<StudentCourse> actual = sut.findCourseOfSelectedStudent(1);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void 受講生情報の存在有無を確認できること() {
    // 存在する受講生IDを確認
    int actual = sut.existStudentByStudentId(1);
    assertThat(actual).isEqualTo(1);

    // 存在しない受講生IDを確認
    actual = sut.existStudentByStudentId(999);
    assertThat(actual).isEqualTo(0);
  }
}
