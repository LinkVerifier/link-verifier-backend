package kjm.linkverifier.auth.web.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class SignInFailureResponse {
    @NonNull
    public String email = "Wrong email";
    public String password = "Wrong password";

    public SignInFailureResponse(String email) {
        this.email = email;
    }
}
