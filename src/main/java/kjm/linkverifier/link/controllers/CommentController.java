package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Comment updateUsersWhoLikeComment(@PathVariable String id,
                                             HttpServletRequest httpServletRequest) {

        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.likeUnlikeComment(comment, user);
//        linkService.updateLikesInComment(comment, newComment);
//        userService.updateLikesInComment(comment, newComment);

        return commentService.save(newComment);
    }

    @PutMapping("/{id}/dislike")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Comment updateUsersWhoDislikeComment(@PathVariable String id,
                                                HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.dislikeUndislikeComment(comment, user);
        return commentService.save(newComment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id,
                                    HttpServletRequest httpServletRequest) {
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment comment = commentService.findById(id);
        if(user.getComments().contains(comment)) {
            commentService.deleteById(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public List<Comment> getAll(@RequestParam(required = false) String search,
                                @RequestParam(required = false) String to) {

        if(search.equals("date")) {
            if(to != null) {
                return commentService.findAllByOrderByCreationDateDesc(0, Integer.parseInt(to));
            }
            return commentService.findAllByOrderByCreationDateDesc();
        }
        return commentService.findAll();
    }


}
