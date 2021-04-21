package kjm.linkverifier.files.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ResponseFile {

    private String name;

    private String url;

    private String type;

    private long size;
}
