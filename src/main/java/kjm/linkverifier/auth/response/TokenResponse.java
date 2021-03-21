package kjm.linkverifier.auth.response;

import java.util.List;

public class TokenResponse {
    private String id;
    private String username;
    private String email;
    private String token;
    private String profilePhoto;
    private List<String> roles;
    private String type = "Bearer";

    public TokenResponse(String accessToken, String id, String username, String profilePhoto,
                         String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

}
