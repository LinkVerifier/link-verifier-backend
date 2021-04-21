package kjm.linkverifier.auth.web.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    @NonNull
    private String token;
}
