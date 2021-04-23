package kjm.linkverifier.auth.exceptions;

public class PasswordsNotMatchException extends RuntimeException {

    public PasswordsNotMatchException(String message) {
        super(message);
    }

    public PasswordsNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
