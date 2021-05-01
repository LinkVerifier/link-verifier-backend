package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.link.exceptions.UserCommentExistsException;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import kjm.linkverifier.link.requests.LinkRequest;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        currentLink.setViews(currentLink.getViews()+1);
        linkService.save(currentLink);
        return currentLink.getId();
    }

    @GetMapping
    public List<Link> showLinks(@RequestParam(required = false) String search,
                                @RequestParam(required = false) String to) {
        if(to != null) {
            int toInt = Integer.parseInt(to);
            switch (search) {
                case "new":
                    return linkService.findAllByOrderByCreationDateDesc(0, toInt);
                case "most_visited":
                    return linkService.findAllByOrderByViewsDesc(0, toInt);
                case "most_dangerous":
                    return linkService.findAllByOrderByRatingAsc(0, toInt);
                default:
                    return linkService.findAll();
            }
        } else {
            switch (search) {
                case "new":
                    return linkService.findAllByOrderByCreationDateDesc();
                case "most_visited":
                    return linkService.findAllByOrderByViewsDesc();
                case "most_dangerous":
                    return linkService.findAllByOrderByRatingAsc();
                default:
                    return linkService.findAll();
            }
        }
    }

    @GetMapping("/{id}")
    public Link getLinkDetails(@PathVariable("id") String id) {
        return linkService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> addViews(@PathVariable("id") String id) {
        linkService.addViews(id);
        return ResponseEntity.ok("resource address updated");
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Link addComment(@PathVariable("id") String id,
                           @RequestBody CommentRequest commentRequest,
                           HttpServletRequest http) {
        Link link = linkService.findById(id);
        User user = CurrentUser.getCurrentUser(http);
        Comment comment = commentService.getCommentFromCommentRequest(link, user, commentRequest);
        //return commentService.addComment(id, http, commentRequest);

        List<Comment> commentLinkList = link.getComments();
        List<Comment> commentUserList = user.getComments();
        log.info(commentLinkList.size() + " XD");
        if (!commentLinkList.isEmpty() && commentLinkList.stream().anyMatch(c -> c.getUserId().equals(user.getId()))) {
            throw new UserCommentExistsException("User " + user.getEmail() + " can not add more than one comment");
        }
        commentLinkList.add(comment);
        commentUserList.add(comment);
        commentService.save(comment);
        userService.save(user);
        link.setComments(commentRepository.findAllByLinkIdOrderByCreationDateDesc(id));
        user.setComments(commentUserList);
        int rating = linkService.calculateRatings(link.getComments());
        link.setRating(rating);
        return linkService.save(link);
    }
}
