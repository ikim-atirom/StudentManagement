package raisetech.StudentManagement;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  //　MySQLの記法
  //　*=全件　WHERE=キーワード検索で指定　#{name}=searchByNameの引数nameで検索
  @Select("SELECT * FROM student WHERE name = #{name}")
  Student searchByName(String name);

  @Select("SELECT * FROM student")
  List<Student> getAllStudents();

  //　Insert=引数なし=void
  @Insert("INSERT student values(#{name}, #{age}, #{job})")
  void registerStudent(String name, int age, String job);

  @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
  void updateStudentAge(String name, int age, String job);

  @Update("UPDATE student SET job = #{job} WHERE name = #{name}")
  void updateStudentJob(String name, int age, String job);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);
}
