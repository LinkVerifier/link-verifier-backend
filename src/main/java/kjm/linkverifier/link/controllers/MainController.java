package kjm.linkverifier.link.controllers;

import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.requests.LinkRequest;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<?> searchLink(@Valid @RequestBody LinkRequest linkRequest) {
        if (!linkService.existsByLink(linkRequest.getLink())) {
            linkService.save(new Link(linkRequest.getLink(), 1, 0));
        }

        Link currentLink = linkService.findByName(linkRequest.getLink())
                .orElseThrow(() -> new RuntimeException("Error: Link is not found."));

        return ResponseEntity.ok("Wysłany link: "
                + linkService.existsByLink(linkRequest.getLink())
                + " : " + linkRequest.getLink());
    }


}
