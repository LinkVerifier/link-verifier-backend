package kjm.linkverifier.link.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "opinions")
public class Opinion {
    @Id
    private String id;

    private OpinionEnum name;
}
