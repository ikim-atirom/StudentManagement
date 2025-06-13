package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
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
