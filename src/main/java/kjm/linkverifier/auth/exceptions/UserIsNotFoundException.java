package kjm.linkverifier.auth.exceptions;

public class UserIsNotFoundException extends RuntimeException {
    public UserIsNotFoundException(String id) {
        super("User with id: " + id + " not found");
    }
}
