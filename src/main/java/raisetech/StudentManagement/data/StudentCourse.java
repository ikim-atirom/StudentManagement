package raisetech.StudentManagement.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"courseId", "courseName", "studentId", "startDate", "endDate"})
public class StudentCourse {

  @JsonProperty("courseId")
  private String courseId;
  @JsonProperty("courseName")
  private String courseName;
  private Integer studentId;
  private LocalDate startDate;
  private LocalDate endDate;
}
