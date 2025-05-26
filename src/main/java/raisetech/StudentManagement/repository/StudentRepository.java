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

@Mapper
public interface StudentRepository {  //DB

  @Select("SELECT * FROM students")
  List<Student> searchStudent();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchCourse();

  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> findCourseOfSelectedStudent(int studentId);

  @Insert("INSERT INTO students (full_name, kana_name, nickname, age, gender, email, address, remark, isDeleted)" +
          "VALUES (#{fullName}, #{kanaName}, #{nickname}, #{age}, #{gender}, #{email}, #{address}, #{remark}, false)")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void registerStudent(Student student);

  @Insert("INSERT INTO students_courses (student_id, course_name, start_date, end_date)" +
          "VALUES (#{studentId}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void registerStudentCourse(StudentCourse studentCourse);

  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student findStudentByStudentId(int studentId);

  @Select("SELECT course_name FROM students_courses WHERE student_id = #{studentId}")
  List<String> findSelectedCoursesByStudentId(int studentId);

  @Update("UPDATE students SET full_name = #{fullName}, kana_name = #{kanaName}, nickname = #{nickname}, age = #{age}, gender = #{gender}, email = #{email}, address = #{address}, remark = #{remark}, isDeleted = #{isDeleted} WHERE student_id = #{studentId}")
  void updateStudent(Student student);

  @Delete("DELETE FROM students_courses WHERE course_id = #{courseId}")
  void deleteStudentCourseByCourseId(int courseId);

  @Select("SELECT * FROM students WHERE isDeleted = false")
  List<Student> searchAllActiveStudents();
}
