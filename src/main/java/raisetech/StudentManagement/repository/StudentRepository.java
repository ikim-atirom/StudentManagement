package raisetech.StudentManagement.repository;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;

/**
 * 受講生情報を管理（一覧検索、詳細情報表示、新規登録、更新）するリポジトリです。
 */
@Mapper
public interface StudentRepository {  //DB

  /**
   * 受講生の一覧を全件検索します。
   *
   * @return 受講生一覧
   */
  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  /**
   * 受講生のコース情報を全件検索します。
   * @return 受講生のコース情報
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchCourse();

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> findCourseOfSelectedStudent(int studentId);

  /**
   * 受講生を新規登録します。
   *
   * @param student 登録する受講生情報
   */
  @Insert("INSERT INTO students (full_name, kana_name, nickname, age, gender, email, address, remark, isDeleted)" +
          "VALUES (#{fullName}, #{kanaName}, #{nickname}, #{age}, #{gender}, #{email}, #{address}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  /**
   * 受講生のコースを登録します。
   *
   * @param studentCourse 登録する受講生のコース情報
   */
  @Insert("INSERT INTO students_courses (student_id, course_name, start_date, end_date)" +
          "VALUES (#{studentId}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * IDに紐づく受講生の詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細情報
   */
  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student findStudentByStudentId(int studentId);

  /**
   * 受講生が選択したコース名を取得します。
   *
   * @param studentId 受講生ID
   * @return 選択されたコース名のリスト
   */
  @Select("SELECT course_name FROM students_courses WHERE student_id = #{studentId}")
  List<String> findSelectedCoursesByStudentId(int studentId);

  /**
   * 受講生の情報を更新します。
   *
   * @param student 更新する受講生情報
   */
  @Update("UPDATE students SET full_name = #{fullName}, kana_name = #{kanaName}, nickname = #{nickname}, age = #{age}, gender = #{gender}, email = #{email}, address = #{address}, remark = #{remark}, isDeleted = #{isDeleted} WHERE student_id = #{studentId}")
  void updateStudent(Student student);

  /**
   * IDに紐づく受講生のコース情報を削除します。
   */
  @Delete("DELETE FROM students_courses WHERE course_id = #{courseId}")
  void deleteStudentCourseByCourseId(int courseId);

  /**
   * アクティブな受講生の一覧を検索します。
   */
  @Select("SELECT * FROM students WHERE isDeleted = false")
  List<Student> searchAllActiveStudents();
}
