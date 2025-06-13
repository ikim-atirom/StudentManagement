package raisetech.StudentManagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Schema(description = "受講生詳細（受講生情報と受講生コース情報）")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  private Student student;
  private List<StudentCourse> registeredCourses;
  private List<String> selectedCourseNames;

  /**
   * 新規登録時の受講生詳細情報を取得します。
   *
   * @param student 受講生情報
   * @param registeredCourses 受講生のコース情報
   */
  public static StudentDetail forRegister(Student student, List<StudentCourse> registeredCourses) {
    return new StudentDetail(student, registeredCourses, new ArrayList<>());
  }

  /**
   * 更新時の受講生の詳細情報を取得します。
   *
   * @param student 受講生情報
   * @param selectedCourseNames 選択されたコースのリスト
   */
  public static StudentDetail forUpdate(Student student, List<String> selectedCourseNames) {
    return new StudentDetail(student, new ArrayList<>(), selectedCourseNames);
  }
}
