package kjm.linkverifier.auth.web.request;

import lombok.Data;

import java.util.Set;

import javax.validation.constraints.*;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank
    @Size(min = 6, max = 100)
    private String passwordConfirmation;

    @NotBlank
    private Long creationDate;
}
