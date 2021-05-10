package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.LinkRequest;
import kjm.linkverifier.link.response.MessageResponse;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/links")
@Slf4j
public class LinkController {

    @Autowired
    OpinionRepository opinionRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LinkService linkService;

    @PostMapping
    public String searchLink(@Valid @RequestBody LinkRequest linkRequest) {
        String link = linkService.cleanURL(linkRequest.getLinkName());
        if (!linkService.existsByLink(link)) {
            linkService.save(new Link(link, 0, 0, new Date(linkRequest.getDeliveryDate()), new Date(linkRequest.getDeliveryDate())));
        }

        Link currentLink = linkService.findByName(link);
        int rating = linkService.calculateRatings(currentLink.getComments());
        currentLink.setRating(rating);
        currentLink.setLastVisitDate(new Date(linkRequest.getDeliveryDate()));
        linkService.save(currentLink);
        return currentLink.getId();
    }

    @GetMapping
    public ResponseEntity<?> showLinks(@RequestParam(required = false) String search,
                                @RequestParam(required = false) String to,
                                @RequestParam(required = false) String commentId) {
        if(to != null) {
            int toInt = Integer.parseInt(to);
            switch (search) {
                case "new":
                    return new ResponseEntity<>(linkService.findAllByOrderByCreationDateDesc(0, toInt), HttpStatus.OK);
                case "most_visited":
                    return new ResponseEntity<>(linkService.findAllByOrderByViewsDesc(0, toInt), HttpStatus.OK);
                case "most_dangerous":
                    return new ResponseEntity<>(linkService.findAllByOrderByRatingAsc(0, toInt), HttpStatus.OK);
                default:
                    return new ResponseEntity<>(linkService.findAll(), HttpStatus.OK);
            }
        } else {
            if(commentId != null) {
                Comment comment = commentService.findById(commentId);
                return new ResponseEntity<>(linkService.findByCommentsContaining(comment), HttpStatus.OK);
            }
            return new ResponseEntity<>(linkService.findAll(), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public Link getLinkDetails(@PathVariable("id") String id) {
        return linkService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> addViews(@PathVariable("id") String id) {
        linkService.addViews(id);
        return ResponseEntity.ok(new MessageResponse("resource address updated"));
    }

}
