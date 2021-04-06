package kjm.linkverifier.link.requests;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    private String comment;

    private Long date;

    private String opinion;
}
