package kjm.linkverifier.auth.models;

import kjm.linkverifier.link.model.Comment;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@RequiredArgsConstructor
public class User {

    public User(String username, String email, String password, String profilePicture) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePicture = profilePicture;
        this.roles = new HashSet<>() {{ new Role(RoleEnum.ROLE_USER); }};
    }

    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    @NonNull
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    @NonNull
    private String email;

    @NotBlank
    @Size(max = 120)
    @NonNull
    private String password;

    @NonNull
    private String profilePicture;

    @NonNull
    private Date creationDate;

    @DBRef
    private Set<Role> roles;

    @DBRef
    private List<Comment> comments;

    @NonNull
    private boolean isConfirmed = false;
}