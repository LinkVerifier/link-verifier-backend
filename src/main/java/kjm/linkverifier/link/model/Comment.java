package kjm.linkverifier.link.model;

import kjm.linkverifier.auth.models.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    private Set<String> usersWhoLike;

    private Set<String> usersWhoDislike;
}
