package raisetech.StudentManagement.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private String courseId;
  private String courseName;
  private Integer studentId;
  private LocalDate startDate;
  private LocalDate endDate;
}
