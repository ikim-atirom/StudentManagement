package raisetech.StudentManagement.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudent();
  }

  // 使わなさそうだったら消す
  public List<Student> search30sStudentList() {
    List<Student> students = repository.searchStudent();
    return students.stream()
        .filter(student -> student.getAge() >= 30 && student.getAge() <40)
        .collect(Collectors.toList());
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchCourse();
  }

  // 使わなさそうだったら消す
  public List<StudentCourse> searchJavaCourseList() {
    List<StudentCourse> studentCourses = repository.searchCourse();
    return studentCourses.stream().filter(studentCourse -> studentCourse.getCourseName().contains("Java"))
        .collect(Collectors.toList());
  }
}
