package kjm.linkverifier.link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Document(collection = "links")
public class Link {

    @Id
    private String id;

    @NonNull
    private String linkName;

    @NonNull
    private int views;

    @NonNull
    private int rating;

    @NonNull
    private Date creationDate; // data utworzenia linku

    @NonNull
    private Date lastVisitDate; // data ostatniego wejscia w link

    private Date lastCommentDate; // data ostatnio dodanego komentarza

    @DBRef
    private List<Comment> comments = new ArrayList<>();

}
