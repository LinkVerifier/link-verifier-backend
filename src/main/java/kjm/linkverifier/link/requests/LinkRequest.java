package kjm.linkverifier.link.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LinkRequest {

    @NotBlank
    private String link;

    @NotBlank
    private Long deliveryDate;

}
