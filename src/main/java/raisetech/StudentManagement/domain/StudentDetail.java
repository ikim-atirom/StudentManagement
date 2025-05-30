package raisetech.StudentManagement.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  private Student student;
  private List<StudentCourse> studentsCourses;
  private List<String> selectedCourseNames;
  // htmlで受け取った情報を色分けする必要なかったかも…List<StudentCourse> studentCoursesに統一したほうがよさそう
}
