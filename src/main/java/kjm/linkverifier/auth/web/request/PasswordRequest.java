package kjm.linkverifier.auth.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordRequest {

    String oldPassword;

    String newPassword;

    String newRepeatedPassword;
}
