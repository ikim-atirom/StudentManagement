package raisetech.StudentManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonPropertyOrder({"cId", "cName", "stId", "startDate", "endDate"})
public class StudentCourse {

  @JsonProperty("cId")
  private String cId;
  @JsonProperty("cName")
  private String cName;

  private int stId;
  private LocalDate startDate;
  private LocalDate endDate;
}
