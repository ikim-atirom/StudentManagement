package raisetech.StudentManagement.exception;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 例外ハンドラーです。
 * 受講生情報の取得や登録、更新時に発生する可能性のある例外を処理します。
 */
@ControllerAdvice
public class ControllerExceptionHandler {

  /**
   * 受講生情報が存在しない場合の例外を処理します。
   *
   * @param ex 受講生情報が存在しない場合の例外
   * @return 404 Not Foundのレスポンス
   */
  @org.springframework.web.bind.annotation.ExceptionHandler
  public ResponseEntity<Map<String, String>> handleStudentNotFoundException(StudentNotFoundException ex) {
    Map<String, String> errorResponse = new LinkedHashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now().toString());
    errorResponse.put("status", HttpStatus.NOT_FOUND.toString());
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  /**
   * 受講生情報の登録や更新時に発生するバリデーションエラーを処理します。
   *
   * @param ex バリデーションエラー
   * @return 400 Bad Requestのレスポンス
   */
  @org.springframework.web.bind.annotation.ExceptionHandler
  public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, Object> errorResponse = new LinkedHashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());

    List<String> errors = new ArrayList<>();
    ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
      errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
    });
    errorResponse.put("error", errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
