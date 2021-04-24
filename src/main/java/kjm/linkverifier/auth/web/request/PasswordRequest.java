package kjm.linkverifier.auth.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

    @NotNull
    String oldPassword;

    @NotNull
    @Size(min = 2, max = 150)
    String newPassword;

    @NotNull
    @Size(min = 2, max = 150)
    String newRepeatedPassword;
}
