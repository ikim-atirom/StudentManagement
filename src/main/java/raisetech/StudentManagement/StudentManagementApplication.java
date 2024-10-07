package raisetech.StudentManagement;

import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;
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
		return "Hello, World!";
	}

	@GetMapping("/test.apache.commons.lang")
	public String test() {
		StringUtils.isNumeric("12345");
		return "数値です";
	}
}
