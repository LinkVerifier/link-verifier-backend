package kjm.linkverifier.link.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class EmailRequest {

    @NonNull
    String email;
}
