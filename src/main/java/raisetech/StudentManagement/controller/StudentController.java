package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.service.StudentService;

@Controller
public class StudentController { // UI層

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentsCourses = service.searchStudentCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
    return "studentList"; // templatesのファイル名
  }

  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.searchStudentCourseList();
  }
}
