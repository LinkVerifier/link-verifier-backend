package kjm.linkverifier.auth.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    @NonNull
    private String token;
}
