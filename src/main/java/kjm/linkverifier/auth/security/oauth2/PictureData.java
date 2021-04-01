package kjm.linkverifier.auth.security.oauth2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureData {
    private String height;
    private String width;
    private String url;
}
