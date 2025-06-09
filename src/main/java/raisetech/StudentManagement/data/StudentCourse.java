package raisetech.StudentManagement.data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentCourse {

  private Integer courseId;

  @NotNull
  private String courseName;

  private Integer studentId;
  private LocalDate startDate;
  private LocalDate endDate;
}
