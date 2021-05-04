package kjm.linkverifier.link.service;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.link.exceptions.UserCommentExistsException;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LinkService linkService;

    @Autowired
    UserService userService;

    @Autowired
    OpinionRepository opinionRepository;

    public Comment findById(String id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Comment is not found"));
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment getCommentFromCommentRequest(Link link, User user, CommentRequest commentRequest) {
        Date date = new Date(commentRequest.getDate());
        String opinionStr = commentRequest.getOpinion();
        Opinion opinion;
        log.info(opinionStr + " <- opinion");
        if(opinionStr == null) {
            opinion = opinionRepository.findByName(OpinionEnum.NEUTRAL)
                    .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
        } else {
            switch (opinionStr) {
                case "VIRUS":
                    opinion = opinionRepository.findByName(OpinionEnum.VIRUS)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "FAKE_NEWS":
                    opinion = opinionRepository.findByName(OpinionEnum.FAKE_NEWS)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "FRAUD":
                    opinion = opinionRepository.findByName(OpinionEnum.FRAUD)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "INDECENT_CONTENT":
                    opinion = opinionRepository.findByName(OpinionEnum.INDECENT_CONTENT)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "SAFE":
                    opinion = opinionRepository.findByName(OpinionEnum.SAFE)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "RELIABLE":
                    opinion = opinionRepository.findByName(OpinionEnum.RELIABLE)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                default:
                    opinion = opinionRepository.findByName(OpinionEnum.NEUTRAL)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
            }
        }
        return new Comment(commentRequest.getComment(), user.getId(), link.getId(), date, opinion);
    }

    // musi zwracać KOMENTARZ
    public Link addComment(String id, HttpServletRequest http, CommentRequest commentRequest) {
        Link link = linkService.findById(id);
        User user = CurrentUser.getCurrentUser(http);
        Comment comment = getCommentFromCommentRequest(link, user, commentRequest);
        //return commentService.addComment(id, http, commentRequest);

        List<Comment> commentLinkList = link.getComments();
        List<Comment> commentUserList = user.getComments();
        if (commentLinkList.stream().anyMatch(c -> c.getUserId().equals(user.getId()))) {
            throw new UserCommentExistsException("User " + user.getEmail() + " can not add more than one comment");
        }
        commentLinkList.add(comment);
        commentUserList.add(comment);
        save(comment);
        userService.save(user);
        link.setComments(commentRepository.findAllByLinkIdOrderByCreationDateDesc(id));
        user.setComments(commentUserList);
        int rating = linkService.calculateRatings(link.getComments());
        link.setRating(rating);
        return linkService.save(link);
    }

    public Comment likeUnlikeComment(Comment comment, User user) {
        Set<String> usersWhoLikeSet = comment.getUsersWhoLike();
        Set<String> usersWhoDislikeSet = comment.getUsersWhoDislike();
        if (usersWhoLikeSet.contains(user.getId())) {
            usersWhoLikeSet.remove(user.getId());
            comment.setUsersWhoLike(usersWhoLikeSet);
        } else {
            if (usersWhoDislikeSet == null) {
                usersWhoDislikeSet = new HashSet<>();
            }
            usersWhoDislikeSet.remove(user.getId());
            usersWhoLikeSet.add(user.getId());
            comment.setUsersWhoLike(usersWhoLikeSet);
            comment.setUsersWhoDislike(usersWhoDislikeSet);
        }
        return comment;
    }

    public Comment dislikeUndislikeComment(Comment comment, User user) {
        Set<String> usersWhoLikeSet = comment.getUsersWhoLike();
        Set<String> usersWhoDislikeSet = comment.getUsersWhoDislike();

        if (usersWhoDislikeSet.contains(user.getId())) {
            usersWhoDislikeSet.remove(user.getId());
            comment.setUsersWhoDislike(usersWhoDislikeSet);
        } else {
            if (usersWhoLikeSet == null) {
                usersWhoLikeSet = new HashSet<>();
            }
            usersWhoLikeSet.remove(user.getId());
            usersWhoDislikeSet.add(user.getId());
            comment.setUsersWhoLike(usersWhoLikeSet);
            comment.setUsersWhoDislike(usersWhoDislikeSet);
        }

        return comment;
    }

    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findAllByOrderByCreationDateDesc() {
        return commentRepository.findAllByOrderByCreationDateDesc();
    }

    public List<Comment> findAllByOrderByCreationDateDesc(int from, int to) {
        if(commentRepository.findAllByOrderByCreationDateDesc().size()<to) {
            return commentRepository.findAllByOrderByCreationDateDesc();
        }
        return commentRepository.findAllByOrderByCreationDateDesc().subList(from, to);
    }

    public List<Comment> findAllByLinkIdOrderByCreationDateDesc(String id) {
        return commentRepository.findAllByLinkIdOrderByCreationDateDesc(id);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}
