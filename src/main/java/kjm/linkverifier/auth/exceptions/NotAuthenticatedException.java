package kjm.linkverifier.auth.exceptions;

public class NotAuthenticatedException extends RuntimeException {
    public NotAuthenticatedException(String email) {
        super("User with email: " + email + " is not authenticated.");
    }
}
