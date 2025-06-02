package raisetech.StudentManagement.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.StudentManagement.controller.converter.StudentConverter;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.domain.StudentDetail;
import raisetech.StudentManagement.service.StudentService;

@RestController
public class StudentController { // UI層

  private StudentService service;
  private StudentConverter converter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter) {
    this.service = service;
    this.converter = converter;
  }

  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<StudentCourse> studentsCourses = service.searchStudentCourseList();
    return converter.convertStudentDetails(students, studentsCourses);
  }

  @GetMapping("/activeStudentList")
  public String getActiveStudentList(Model model) {
    List<Student> activeStudents = service.searchActiveStudentList();
    List<StudentCourse> studentsCourses = service.searchStudentCourseList();

    model.addAttribute("studentList", converter.convertStudentDetails(activeStudents, studentsCourses));
    return "studentList";
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

  @PostMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    service.addStudentCourse(studentDetail);
    service.deleteStudentCourse(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }
}
