package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.security.services.CurrentUser;
import kjm.linkverifier.auth.security.services.UserDetailsImpl;
import kjm.linkverifier.auth.security.services.UserService;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/{id}")
    public Link addComment(@PathVariable("id") String id, @RequestBody CommentRequest commentRequest,
                           HttpServletRequest http) {
        Link link = linkService.findById(id);
        User user = CurrentUser.getCurrentUser(http);
        log.info(commentRequest.getComment(), commentRequest.getOpinion(), commentRequest.getDate());
        Comment comment = commentService.getCommentFromCommentRequest(link, user, commentRequest);

        List<Comment> commentLinkList = link.getComments();
        List<Comment> commentUserList = user.getComments();
        if(commentLinkList == null) {
            commentLinkList = new ArrayList<>();
        }
        if (commentUserList == null) {
            commentUserList = new ArrayList<>();
        }
        commentLinkList.add(comment);
        commentUserList.add(comment);
        link.setComments(commentLinkList);
        user.setComments(commentUserList);
        commentRepository.save(comment);
        userService.save(user);
        return linkService.save(link);
    }


    @GetMapping("/{id}")
    public Link getLinkDetails(@PathVariable("id") String id) {
        return linkService.findById(id);
    }
}
