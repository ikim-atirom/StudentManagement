package raisetech.StudentManagement.repository;

import java.util.List;
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

  @Insert("INSERT INTO students (full_name, kana_name, nickname, age, gender, email, address)" +
          "VALUES (#{fullName}, #{kanaName}, #{nickname}, #{age}, #{gender}, #{email}, #{address})")
  @Options(useGeneratedKeys = true, keyProperty = "studentId")
  void insertStudent(Student student);

  @Insert("INSERT INTO students_courses (student_id, course_name, start_date, end_date)" +
          "VALUES (#{studentId}, #{courseName}, #{startDate}, #{endDate})")
  @Options(useGeneratedKeys = true, keyProperty = "courseId")
  void insertStudentCourse(StudentCourse studentCourse);

  @Select("SELECT * FROM students WHERE student_id = #{studentId}")
  Student searchStudentByStudentId(int studentId);

  @Select("SELECT course_name FROM students_courses WHERE student_id = #{studentId}")
  List<String> getSelectedCoursesByStudentId(int studentId);

  @Update("UPDATE students SET full_name = #{fullName}, kana_name = #{kanaName}, nickname = #{nickname}, age = #{age}, gender = #{gender}, email = #{email}, address = #{address} WHERE student_id = #{studentId}")
  int updateStudent(Student student);
}
