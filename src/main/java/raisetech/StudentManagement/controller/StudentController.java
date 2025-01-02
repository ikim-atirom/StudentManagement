package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
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

  @GetMapping("/newStudent")
  public String newStudent(Model model) {
    model.addAttribute("studentDetail", new StudentDetail());
    return "registerStudent";
  }

  @PostMapping("/registerStudent")
  public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    if (result.hasErrors()) {
      result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
      return "registerStudent";
    }

    // 受講生登録、受講生IDを取得、受講生コース登録
    service.registerStudent(studentDetail);
    Integer studentId = studentDetail.getStudent().getStudentId();
    service.registerStudentCourses(studentId, studentDetail.getSelectedCourseNames());

    return "redirect:/studentList";
  }

  @GetMapping("/studentInfo/{studentId}")
  public String getStudentInfo(@PathVariable("studentId") Integer studentId, Model model) {
    Student student = service.searchStudentByStudentId(studentId);
    List<String> selectedCourses = service.getSelectedCoursesByStudentId(studentId);

    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudent(student);
    studentDetail.setSelectedCourseNames(selectedCourses);

    model.addAttribute("studentDetail", studentDetail);
    return "updateStudent";
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
    System.out.println(studentDetail.getSelectedCourseNames());
    if (result.hasErrors()) {
      result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
      return "updateStudent";
    }

    // 受講生情報、コース情報更新
    service.updateStudent(studentDetail);
    service.addStudentCourse(studentDetail);
    service.deleteStudentCourse(studentDetail);
    return "redirect:/studentList";
  }
}
