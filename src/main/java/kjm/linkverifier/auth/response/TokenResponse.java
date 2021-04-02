package kjm.linkverifier.auth.response;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class TokenResponse {
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String token;
    @NonNull
    private String profilePhoto;
    @NonNull
    private List<String> roles;
    private String type = "Bearer";

    public TokenResponse(String token, String id, String username, String profilePhoto,
                         String email, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.email = email;
        this.roles = roles;
    }
}
