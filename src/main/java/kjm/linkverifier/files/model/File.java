package kjm.linkverifier.files.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "files")
public class File {

    @Id
    private String id;

    @NonNull
    private String name;

    @NonNull
    private String type;

    @NonNull
    private byte[] data;

}
