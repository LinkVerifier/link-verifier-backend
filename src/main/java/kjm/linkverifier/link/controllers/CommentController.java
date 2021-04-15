package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.security.services.CurrentUser;
import kjm.linkverifier.auth.security.services.UserDetailsImpl;
import kjm.linkverifier.auth.security.services.UserService;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private LinkService linkService;

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable String id) {
        return commentService.findById(id);
    }


    @GetMapping("/{id}/like")
    public Comment updateUsersWhoLikeComment(@PathVariable String id,
                                             HttpServletRequest httpServletRequest) {

        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.likeComment(comment, user);
//        linkService.updateLikesInComment(comment, newComment);
//        userService.updateLikesInComment(comment, newComment);

        return commentService.save(newComment);
    }

    @GetMapping("/{id}/unlike")
    public Comment deleteLike(@PathVariable String id,
                              HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.unlikeComment(comment, user);
        return commentService.save(newComment);
    }

    @GetMapping("/{id}/dislike")
    public Comment updateUsersWhoDislikeComment(@PathVariable String id,
                                                HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.dislikeComment(comment, user);
        return commentService.save(newComment);
    }

    @GetMapping("/{id}/undislike")
    public Comment deleteDislike(@PathVariable String id,
                                 HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.undislikeComment(comment, user);
        return commentService.save(newComment);
    }
}
