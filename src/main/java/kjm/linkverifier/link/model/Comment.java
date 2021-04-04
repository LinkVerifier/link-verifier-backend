package kjm.linkverifier.link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "comments")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    String id;

    @NonNull
    String comment;

    @NonNull
    Date creationDate;

    @NonNull
    Opinion opinion;
}
