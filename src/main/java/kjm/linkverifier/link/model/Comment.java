package kjm.linkverifier.link.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comments")
public class Comment {

    public Comment(String  comment, Date creationDate, Opinion option) {
        this.comment = comment;
        this.creationDate = creationDate;
        this.opinion = option;
    }

    @Id
    private String id;

    @NonNull
    private String comment;

    @NonNull
    private Date creationDate;

    @NonNull
    @DBRef
    private Opinion opinion;

    private Set<String> usersWhoLike = new HashSet<>();

    private Set<String> usersWhoDislike = new HashSet<>();
}
