package raisetech.StudentManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生一覧の全件検索_リポジトリとコンバーターの処理が適切に呼び出せていること() {
    List<Student> students = new ArrayList<>();
    List<StudentCourse> studentCourses = new ArrayList<>();

    when(repository.searchStudent()).thenReturn(students);
    when(repository.searchCourse()).thenReturn(studentCourses);

    sut.searchStudents();

    verify(repository, times(1)).searchStudent();
    verify(repository, times(1)).searchCourse();
    verify(converter, times(1)).convertStudentDetails(students, studentCourses);
  }

  @Test
  void IDに紐づく受講生の詳細情報を取得_リポジトリの処理が適切に呼び出せていること() {
    Student student = new Student();
    student.setStudentId(0);
    student.setFullName("テストネーム");
    List<String> selectedCourses = List.of("テストコース");

    when(repository.findStudentByStudentId(0)).thenReturn(student);
    when(repository.findSelectedCoursesByStudentId(0)).thenReturn(selectedCourses);

    StudentDetail actual = sut.getStudentInfo(0);

    assertEquals("テストネーム", actual.getStudent().getFullName());
    assertEquals("テストコース", actual.getSelectedCourseNames().get(0));
  }

  @Test
  void アクティブな受講生一覧の検索_リポジトリの処理が適切に呼び出せていること() {
    List<Student> activeStudents = new ArrayList<>();

    when(repository.searchAllActiveStudents()).thenReturn(activeStudents);

    sut.searchActiveStudents();

    verify(repository, times(1)).searchAllActiveStudents();
  }

  @Test
  void 受講生のコース情報を全件検索_リポジトリの処理が適切に呼び出せていること() {
    List<StudentCourse> studentCourses = new ArrayList<>();

    when(repository.searchCourse()).thenReturn(studentCourses);

    sut.searchStudentCourses();

    verify(repository, times(1)).searchCourse();
  }

  @Test
  void 受講生を新規登録_リポジトリの処理が適切に呼び出せていること() {
    StudentDetail studentDetail = new StudentDetail();
    Student student = new Student();
    List<String> selectedCourses = List.of("新規コース1", "新規コース2");

    studentDetail.setStudent(student);
    studentDetail.setSelectedCourseNames(selectedCourses);

    // DBで自動採番するIDをモックで設定
    doAnswer(invocation -> {
      Student dummyStudent = invocation.getArgument(0);
      dummyStudent.setStudentId(999);
      return null;
    }).when(repository).registerStudent(any(Student.class));

    sut.registerStudent(studentDetail);

    verify(repository, times(1)).registerStudent(any(Student.class));
    verify(repository, times(selectedCourses.size())).registerStudentCourse(any(StudentCourse.class));
    verify(repository, times(1)).findCourseOfSelectedStudent(anyInt());
  }

  @Test
  void 受講生コース情報を新規登録_リポジトリの処理が適切に呼び出せていること() {
    Integer studentId = 0;
    List<String> registeredCourses = List.of("新規コース1", "新規コース2", "新規コース3");

    sut.registerStudentCourses(studentId, registeredCourses);

    verify(repository, times(registeredCourses.size())).registerStudentCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生詳細の更新_サービスの処理が適切に呼び出せていること() {
    // 同じサービスクラス内のメソッドを呼び出すためスパイ化する
    StudentService spySut = spy(new StudentService(repository, converter));
    StudentDetail studentDetail = new StudentDetail();
    Student student = new Student();
    student.setStudentId(0);
    studentDetail.setStudent(student);
    studentDetail.setSelectedCourseNames(List.of("更新コース1", "更新コース2"));

    spySut.updateStudentDetail(studentDetail);

    verify(spySut, times(1)).updateStudent(studentDetail);
    verify(spySut, times(1)).addStudentCourse(studentDetail);
    verify(spySut, times(1)).deleteStudentCourse(studentDetail);
  }

  @Test
  void 受講生のコース情報を追加_リポジトリの処理が適切に呼び出されていること() {
    StudentDetail studentDetail = new StudentDetail();
    Student student = new Student();
    student.setStudentId(0);
    studentDetail.setStudent(student);

    // 既存のコース情報をモックで設定
    StudentCourse existingCourse = new StudentCourse();
    existingCourse.setStudentId(0);
    existingCourse.setCourseName("更新コース1");
    existingCourse.setStartDate(null);
    existingCourse.setEndDate(null);
    List<StudentCourse> existingCourses = List.of(existingCourse);

    // 新規に追加するコース情報を設定
    List<String> selectedCourses = List.of("更新コース1", "更新コース2");
    studentDetail.setSelectedCourseNames(selectedCourses);

    when(repository.findCourseOfSelectedStudent(0)).thenReturn(existingCourses);;

    sut.addStudentCourse(studentDetail);

    verify(repository, times(1)).registerStudentCourse(any(StudentCourse.class));
  }

  @Test
  void 受講生のコース情報を削除_リポジトリの処理が適切に呼び出されていること() {
    StudentDetail studentDetail = new StudentDetail();
    Student student = new Student();
    student.setStudentId(0);
    studentDetail.setStudent(student);

    // 既存のコース情報をモックで設定
    StudentCourse course1 = new StudentCourse();
    course1.setCourseId(1);
    course1.setCourseName("更新コース1");
    course1.setStudentId(0);
    StudentCourse course2 = new StudentCourse();
    course2.setCourseId(2);
    course2.setCourseName("更新コース2");
    course2.setStudentId(0);
    List<StudentCourse> existingCourses = List.of(course1, course2);

    when(repository.findCourseOfSelectedStudent(0)).thenReturn(existingCourses);

    // 更新後のコース情報を設定
    studentDetail.setSelectedCourseNames((List.of("更新コース1")));

    sut.deleteStudentCourse(studentDetail);

    verify(repository, times(1)).deleteStudentCourseByCourseId(2);
  }
}
