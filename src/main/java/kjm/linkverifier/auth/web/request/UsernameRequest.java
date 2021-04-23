package kjm.linkverifier.auth.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UsernameRequest {

    @Size(min = 2, max = 50)
    @NotNull
    String username;
}
