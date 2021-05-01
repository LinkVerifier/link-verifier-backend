package kjm.linkverifier.link.exceptions;

public class LinkNotFoundException extends RuntimeException {

    public LinkNotFoundException() {
        super("Link is not found");
    }
    public LinkNotFoundException(String message) {
        super(message);
    }


}
