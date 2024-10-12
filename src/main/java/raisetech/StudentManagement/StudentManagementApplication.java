package raisetech.StudentManagement;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	//　Listの初期化
	private List<Student> studentList = new ArrayList<>();
	private Student student = new Student("Luffy", 16 ,"Pirate");

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/studentInfo")
	public String getStudentInfo() {
		return student.getName() + "　" + student.getAge() + "歳" + "　" + student.getJob();
	}

	@PostMapping("/studentInfo")
	public void setStudentInfo(String name, int age, String job) {
		student.setName(name);
		student.setAge(age);
		student.setJob(job);
	}

	@PostMapping("/studentName")
	public void updateStudentName(String name) {
		student.setName(name);
	}

	@PostMapping("/studentAge")
	public void updateStudentAge(int age) {
		student.setAge(age);
	}

	@PostMapping("/studentJob")
	public void updateStudentJob(String job) {
		student.setJob(job);
	}
}
