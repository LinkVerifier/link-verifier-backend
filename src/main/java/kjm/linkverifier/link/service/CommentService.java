package kjm.linkverifier.link.service;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public void deleteSome(List<Comment> comments) {
        //commentRepository.deleteComments(comments);
    }

    public Comment saveCommentFromCommentRequest(CommentRequest commentRequest) {
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
        Comment comment = new Comment(commentRequest.getComment(), date, opinion);
        return save(comment);
    }

    public List<Comment> findAllByLinkOrderByCreationDateDesc(Link link) {
        List<Comment> comments = link.getComments();
        log.info("wszystkie: {}", comments);
        comments.sort(Comparator.comparing(Comment::getCreationDate).reversed());
        log.info("posortowane: {}", comments);
        return comments;
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
        Link link = linkService.findById(id);
        return findAllByLinkOrderByCreationDateDesc(link);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
}
