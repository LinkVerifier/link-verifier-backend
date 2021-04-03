package kjm.linkverifier.link.requests;

import lombok.Data;

@Data
public class CommentRequest {

    private String comment;

    private Long date;

    private String opinion;
}
