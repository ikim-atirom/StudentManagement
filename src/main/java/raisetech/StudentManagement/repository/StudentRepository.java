package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;

/**
 * 受講生情報を管理（一覧検索、詳細情報表示、新規登録、更新）するリポジトリです。
 */
@Mapper
public interface StudentRepository {  //DB

  /**
   * 受講生の全件検索します。
   *
   * @return 受講生一覧
   */
  List<Student> searchStudent();

  /**
   * 受講生のコース情報を全件検索します。
   *
   * @return 受講生のコース情報
   */
  List<StudentCourse> searchCourse();

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  List<StudentCourse> findCourseOfSelectedStudent(int studentId);

  /**
   * アクティブな受講生を検索します。
   */
  List<Student> searchAllActiveStudents();

  /**
   * 受講生を新規登録します。受講生IDは自動採番されます。
   *
   * @param student 登録する受講生情報
   */
  void registerStudent(Student student);

  /**
   * 受講生コースを登録します。コースIDは自動採番されます。
   *
   * @param studentCourse 登録する受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  Student findStudentByStudentId(int studentId);

  /**
   * 受講生が選択したコース名を取得します。
   *
   * @param studentId 受講生ID
   * @return 選択されたコース名のリスト
   */
  List<String> findSelectedCoursesByStudentId(int studentId);

  /**
   * 受講生の情報を更新します。
   *
   * @param student 更新する受講生情報
   */
  void updateStudent(Student student);

  /**
   * IDに紐づく受講生のコース情報を削除します。
   */
  void deleteStudentCourseByCourseId(int courseId);

  /**
   * 受講生情報の存在有無を確認します。
   *
   * @param studentId 受講生ID
   */
  int existStudentByStudentId(int studentId);
}
