package raisetech.StudentManagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private Integer studentId;
  private String fullName;
  private String kanaName;
  private String nickname;
  private int age;
  private String gender;
  private String email;
  private String address;
  private String remark;
  private boolean isDeleted;
}
