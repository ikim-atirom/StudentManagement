package raisetech.StudentManagement.exception;

/**
 * 受講生が見つからない場合の例外処理
 */
public class StudentNotFoundException extends RuntimeException {

  public StudentNotFoundException() {
    super();
  }

  public StudentNotFoundException(String message) {
    super(message);
  }

  public StudentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public StudentNotFoundException(Throwable cause) {
    super(cause);
  }

}
