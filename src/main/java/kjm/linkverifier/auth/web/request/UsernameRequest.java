package kjm.linkverifier.auth.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernameRequest {

    @Size(min = 2, max = 50)
    @NotNull
    String username;
}
