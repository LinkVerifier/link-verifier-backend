package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable String id) {
        return commentService.findById(id);
    }


    @PutMapping("/{id}/like")
    public Comment updateUsersWhoLikeComment(@PathVariable String id,
                                             HttpServletRequest httpServletRequest) {

        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.likeComment(comment, user);
//        linkService.updateLikesInComment(comment, newComment);
//        userService.updateLikesInComment(comment, newComment);

        return commentService.save(newComment);
    }

    @PutMapping("/{id}/unlike")
    public Comment deleteLike(@PathVariable String id,
                              HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.unlikeComment(comment, user);
        return commentService.save(newComment);
    }

    @PutMapping("/{id}/dislike")
    public Comment updateUsersWhoDislikeComment(@PathVariable String id,
                                                HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.dislikeComment(comment, user);
        return commentService.save(newComment);
    }

    @PutMapping("/{id}/undislike")
    public Comment deleteDislike(@PathVariable String id,
                                 HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.undislikeComment(comment, user);
        return commentService.save(newComment);
    }
}
