package raisetech.StudentManagement;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	//　springが管理しているインスタンスを初期化せず使える
	@Autowired
	private StudentRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/student")
	public String getStudent(@RequestParam String name) {
		Student student = repository.searchByName(name);
		return student.getName() + "　" + student.getAge() + "歳" + "　" + student.getJob();
	}

	@GetMapping("/students")
	public List<Student> getAllStudents() {
		return repository.getAllStudents();
	}

	@PostMapping("/student")
	public void registerStudent(String name, int age, String job) {
		repository.registerStudent(name, age, job);
	}

	@PatchMapping("/student/age")
	public void updateStudentAge(String name, int age, String job) {
		repository.updateStudentAge(name, age, job);
	}

	@PatchMapping("/student/job")
	public void updateStudentJob(String name, int age, String job) {
		repository.updateStudentJob(name, age, job);
	}

	@DeleteMapping("/student")
	public void deleteStudent(String name) {
		repository.deleteStudent(name);
	}
}
