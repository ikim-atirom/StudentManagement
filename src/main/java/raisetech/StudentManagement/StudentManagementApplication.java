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
		String name = "";
		if (StringUtils.isEmpty(name)) {
			//　コンソールに、メソッド呼び出し成功したことを表示
			System.out.println("hello() method was called");
			return "入力してください";
		} else {
			return "Hello," + name + "!";
		}
	}

	@GetMapping("/test.apache.commons.lang")
	public String test() {
		String number = "12345";
		if (StringUtils.isNumeric(number)) {
			//　コンソールに、メソッド呼び出し成功したことを表示
			System.out.println("test() method was called");
			return number + "は数値です";
		} else {
			return number + "は数値ではありません";
		}
	}
}
