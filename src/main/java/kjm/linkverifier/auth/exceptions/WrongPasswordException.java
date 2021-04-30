package kjm.linkverifier.auth.exceptions;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException(String email) {
        super(email);
    }
}
