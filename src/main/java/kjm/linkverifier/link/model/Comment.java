package kjm.linkverifier.link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    @NonNull
    private String comment;

    @NonNull
    private Date creationDate;

    @NonNull
    private Opinion opinion;
}
