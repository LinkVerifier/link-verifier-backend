package kjm.linkverifier.link.requests;

import kjm.linkverifier.link.model.Opinion;
import lombok.Data;

@Data
public class CommentRequest {

    private String comment;

    private Long date;

    private String opinion;
}
