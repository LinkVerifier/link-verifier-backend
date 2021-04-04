package kjm.linkverifier.link.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "opinions")
@RequiredArgsConstructor
public class Opinion {
    @Id
    private String id;

    @NonNull
    private OpinionEnum name;
}
