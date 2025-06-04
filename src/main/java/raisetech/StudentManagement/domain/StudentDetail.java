package raisetech.StudentManagement.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentsCourses;
  private List<String> selectedCourseNames;

  public static StudentDetail forRegister(Student student, List<StudentCourse> studentsCourses) {
    return new StudentDetail(student, studentsCourses, new ArrayList<>());
  }

  public static StudentDetail forUpdate(Student student, List<String> selectedCourseNames) {
    return new StudentDetail(student, null, selectedCourseNames);
  }
}
