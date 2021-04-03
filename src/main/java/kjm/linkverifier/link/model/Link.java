package kjm.linkverifier.link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "links")
public class Link {

    @Id
    private String id;

    @NonNull
    private String link;

    @NonNull
    private int views;

    @NonNull
    private int rating;

//    @DBRef
//    private List<Comment> comments;

}
