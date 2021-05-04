package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.files.response.ResponseMessage;
import kjm.linkverifier.link.exceptions.UserCommentExistsException;
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
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/")
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


    @PutMapping("comments/{id}/like")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Comment updateUsersWhoLikeComment(@PathVariable String id,
                                             HttpServletRequest httpServletRequest) {

        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.likeUnlikeComment(comment, user);

        return commentService.save(newComment);
    }

    @PutMapping("comments/{id}/dislike")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Comment updateUsersWhoDislikeComment(@PathVariable String id,
                                                HttpServletRequest httpServletRequest) {
        Comment comment = commentService.findById(id);
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment newComment = commentService.dislikeUndislikeComment(comment, user);
        return commentService.save(newComment);
    }

    @DeleteMapping("comments/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id,
                                    HttpServletRequest httpServletRequest) {
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        Comment comment = commentService.findById(id);
        Link link = linkService.findLinkByCommentsLike(comment);
        link.getComments().remove(comment);
        link.setRating(linkService.calculateRatings(link.getComments()));
        linkService.save(link);
        user.getComments().remove(comment);
        user.setComments(user.getComments());
        userService.save(user);
        commentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("comments")
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

    @PostMapping("links/{id}/comments")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public Link addComment(@PathVariable("id") String id,
                           @RequestBody CommentRequest commentRequest,
                           HttpServletRequest http) {
        Link link = linkService.findById(id);
        User user = CurrentUser.getCurrentUser(http);
        Comment comment = commentService.getCommentFromCommentRequest(link, user, commentRequest);
        log.info("linkID {}", link.getId());
        log.info("userID {}", user.getId());
        log.info("commentID {}", link.getId());
        List<Comment> commentLinkList = link.getComments();
        List<Comment> commentUserList = user.getComments();
        if (!commentLinkList.isEmpty() && commentLinkList.stream().anyMatch(c -> c.getUserId().equals(user.getId()))) {
            throw new UserCommentExistsException("User " + user.getEmail() + " can not add more than one coment");
        }
        commentLinkList.add(comment);
        commentUserList.add(comment);
        int rating = linkService.calculateRatings(link.getComments());
        link.setRating(rating);
        link.setLastCommentDate(new Date(commentRequest.getDate()));
        commentService.save(comment);
        user.setComments(commentUserList);
        userService.save(user);
        link.setComments(commentService.findAllByLinkIdOrderByCreationDateDesc(id));
        return linkService.save(link);
    }



}
