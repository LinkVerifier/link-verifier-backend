package kjm.linkverifier.link.controllers;

import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.requests.LinkRequest;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Slf4j
public class MainController {

    @Autowired
    private LinkService linkService;

    @GetMapping
    public ResponseEntity<?> showMainPage() {
        log.info("Showing main page");

        return ResponseEntity.ok().body("OK");
    }

    @PostMapping
    public String searchLink(@Valid @RequestBody LinkRequest linkRequest) {
        if (!linkService.existsByLink(linkRequest.getLinkName())) {
            linkService.save(new Link(linkRequest.getLinkName(), 0, 0, new Date(linkRequest.getDeliveryDate())));
        }

        Link currentLink = linkService.findByName(linkRequest.getLinkName())
                .orElseThrow(() -> new RuntimeException("Error: Link is not found."));
        currentLink.setLastVisitDate(new Date(linkRequest.getDeliveryDate()));
        currentLink.setViews(currentLink.getViews()+1);

        linkService.save(currentLink);

        return currentLink.getId();
    }


}
