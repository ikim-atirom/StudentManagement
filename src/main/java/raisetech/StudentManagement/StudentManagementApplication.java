package raisetech.StudentManagement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}

	@GetMapping("/hello")
	public String hello() {
		StringUtils.isEmpty("");
		//　コンソールに、メソッド呼び出し成功したことを表示
		System.out.println("hello() method was called");
		return "Hello, World!";
	}

	@GetMapping("/test.apache.commons.lang")
	public String test() {
		StringUtils.isNumeric("12345");
		//　コンソールに、メソッド呼び出し成功したことを表示
		System.out.println("test() method was called");
		return "数値です";
	}
}
