package raisetech.StudentManagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生情報")
@Getter
@Setter
public class Student {

  private Integer studentId;

  @NotBlank(message = "氏名を入力してください。")
  private String fullName;

  @NotBlank
  @Pattern(regexp = "^(?!.*[ぁ-ん])[ァ-ヶー]+$", message = "全角カタカナで入力してください。")
  private String kanaName;

  private String nickname;

  @NotNull
  @Min(value = 0, message = "年齢は0以上で入力してください。")
  private Integer age;

  @NotBlank
  @Pattern(regexp = "^(男性|女性|回答しない)$", message = "性別は「男性」「女性」「回答しない」のいずれかで入力してください。")
  private String gender;

  @NotBlank
  @Email(message = "正しいメールアドレス形式で入力してください。" )
  @Pattern(
      regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
      message = "メールアドレスは半角英数字で入力してください。"
  )
  private String email;

  @NotBlank(message = "地域を入力してください。")
  private String address;

  private String remark;
  private boolean isDeleted;
}
