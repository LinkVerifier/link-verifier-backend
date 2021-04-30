package kjm.linkverifier.auth.exceptions;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String email) {
        super("User with email: " + email + " is not found");
    }
}
