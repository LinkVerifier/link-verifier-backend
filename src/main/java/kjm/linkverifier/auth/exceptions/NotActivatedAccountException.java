package kjm.linkverifier.auth.exceptions;

public class NotActivatedAccountException extends RuntimeException {
    public NotActivatedAccountException(String email) {
        super("Account with email: " + email + "is not activated");
    }
}
