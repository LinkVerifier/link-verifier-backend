package kjm.linkverifier.link.model;

import org.springframework.data.annotation.Id;


public class Opinion {
    @Id
    private String id;

    private OpinionEnum name;
}
