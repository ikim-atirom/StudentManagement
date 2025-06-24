package raisetech.StudentManagement.controller.converter;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

class StudentConverterTest {

  private StudentConverter sut = new StudentConverter();

  @Test
  void 受講生情報リストと受講生IDに紐づく受講生コース情報リストを結合して受講生詳細情報リストに変換すること() {
    Student studentA = new Student();
    studentA.setStudentId(999);
    studentA.setFullName("テスト太郎");

    Student studentB = new Student();
    studentB.setStudentId(888);
    studentB.setFullName("テスト花子");

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseId(99);
    studentCourse1.setCourseName("テストコース太郎1");
    studentCourse1.setStudentId(999);

    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCourseId(100);
    studentCourse2.setCourseName("テストコース太郎2");
    studentCourse2.setStudentId(999);

    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setCourseId(101);
    studentCourse3.setCourseName("テストコース花子1");
    studentCourse3.setStudentId(888);

    // テスト用の受講生の一覧と受講生コース情報のリストを作成
    List<Student> studentList = new ArrayList<>();
    studentList.add(studentA);
    studentList.add(studentB);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse1);
    studentCourseList.add(studentCourse2);
    studentCourseList.add(studentCourse3);

    // convertStudentDetailsメソッドを呼び出して、受講生詳細情報のリストを取得
    List<StudentDetail> studentDetailList = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証用の太郎と花子の受講生詳細情報を作成
    StudentDetail studentDetailTaro = studentDetailList.stream()
        .filter(detail -> detail.getStudent().getStudentId().equals(999))
        .findFirst()
        .orElseThrow(() -> new AssertionError("太郎の受講生詳細情報が見つかりません"));
    StudentDetail studentDetailHanako = studentDetailList.stream()
        .filter(detail -> detail.getStudent().getStudentId().equals(888))
        .findFirst()
        .orElseThrow(() -> new AssertionError("花子の受講生詳細情報が見つかりません"));

    // 受講生詳細情報のリストが期待される内容と一致するかを検証
    assertAll(
        // 受講生詳細情報のリストが2件であることを確認
        () -> assertEquals(2, studentDetailList.size()),
        // 太郎の受講生詳細情報が正しいことを確認
        () -> assertEquals(999, studentDetailTaro.getStudent().getStudentId()),
        () -> assertEquals("テスト太郎", studentDetailTaro.getStudent().getFullName()),
        () -> assertEquals(2, studentDetailTaro.getRegisteredCourses().size()),
        () -> assertEquals("テストコース太郎1",studentDetailTaro.getRegisteredCourses().get(0).getCourseName()),
        () -> assertEquals("テストコース太郎2", studentDetailTaro.getRegisteredCourses().get(1).getCourseName()),
        // 花子の受講生詳細情報が正しいことを確認
        () -> assertEquals(888, studentDetailHanako.getStudent().getStudentId()),
        () -> assertEquals("テスト花子", studentDetailHanako.getStudent().getFullName()),
        () -> assertEquals(1, studentDetailHanako.getRegisteredCourses().size()),
        () -> assertEquals("テストコース花子1", studentDetailHanako.getRegisteredCourses().get(0).getCourseName())
    );
  }

  @Test
  void 受講生情報が空のとき受講生詳細情報リストも空のリストが返されること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseName("テストコース");

    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    studentCourseList.add(studentCourse);

    List<StudentDetail> studentDetailList = sut.convertStudentDetails(studentList, studentCourseList);

    assertTrue(studentDetailList.isEmpty());
  }

  @Test
  void 受講生コース情報が空のとき受講生情報のみで受講生詳細情報リストが返されること() {
    Student student = new Student();
    student.setStudentId(999);
    student.setFullName("テスト太郎");

    List<Student> studentList = new ArrayList<>();
    studentList.add(student);
    List<StudentCourse> studentCourseList = new ArrayList<>();

    List<StudentDetail> studentDetailList = sut.convertStudentDetails(studentList, studentCourseList);

    assertAll(
        () -> assertEquals(1, studentDetailList.size()),
        () -> assertEquals(999, studentDetailList.get(0).getStudent().getStudentId()),
        () -> assertEquals("テスト太郎", studentDetailList.get(0).getStudent().getFullName()),
        () -> assertTrue(studentDetailList.get(0).getRegisteredCourses().isEmpty())
    );
  }
}