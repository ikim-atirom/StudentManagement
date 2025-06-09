package raisetech.StudentManagement.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生の情報を変換するコンバーターです。
 * 受講生一覧と受講生コース情報を結合して、受講生詳細情報のリストを作成します。
 */
@Component
public class StudentConverter {

  /**
   * 受講生の一覧と受講生のコース情報を結合して、受講生詳細情報のリストを作成します。
   *
   * @param studentList 受講生一覧
   * @param studentCourseList 受講生コース情報のリスト
   * @return 受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList, List<StudentCourse> studentCourseList) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    for (Student student : studentList) {
      List<StudentCourse> filteredCourseList = studentCourseList.stream()
          .filter(studentCourse -> student.getStudentId().equals(studentCourse.getStudentId()))
          .collect(Collectors.toList());

      StudentDetail studentDetail = StudentDetail.forRegister(student, filteredCourseList);
      studentDetails.add(studentDetail);
    }
    return studentDetails;
  }
}
