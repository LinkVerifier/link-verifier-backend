package kjm.linkverifier.link.service;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment likeComment(Comment comment, User user) {
        Set<String> usersWhoLikeSet = comment.getUsersWhoLike();
        Set<String> usersWhoDislikeSet = comment.getUsersWhoDislike();

        if(usersWhoLikeSet == null) {
            usersWhoLikeSet = new HashSet<>();
        }
        if (usersWhoDislikeSet == null) {
            usersWhoDislikeSet = new HashSet<>();
        }
        usersWhoDislikeSet.remove(user.getId());
        usersWhoLikeSet.add(user.getId());
        comment.setUsersWhoLike(usersWhoLikeSet);
        comment.setUsersWhoDislike(usersWhoDislikeSet);
        return comment;
    }

    public Comment unlikeComment(Comment comment, User user) {
        Set<String> usersWhoLikeSet = comment.getUsersWhoLike();

        if(usersWhoLikeSet == null) {
            usersWhoLikeSet = new HashSet<>();
        }
        usersWhoLikeSet.remove(user.getId());
        comment.setUsersWhoLike(usersWhoLikeSet);
        return comment;
    }

    public Comment dislikeComment(Comment comment, User user) {
        Set<String> usersWhoLikeSet = comment.getUsersWhoLike();
        Set<String> usersWhoDislikeSet = comment.getUsersWhoDislike();

        if(usersWhoLikeSet == null) {
            usersWhoLikeSet = new HashSet<>();
        }
        if (usersWhoDislikeSet == null) {
            usersWhoDislikeSet = new HashSet<>();
        }
        usersWhoLikeSet.remove(user.getId());
        usersWhoDislikeSet.add(user.getId());
        comment.setUsersWhoLike(usersWhoLikeSet);
        comment.setUsersWhoDislike(usersWhoDislikeSet);
        return comment;
    }

    public Comment undislikeComment(Comment comment, User user) {
        Set<String> usersWhoDislikeSet = comment.getUsersWhoDislike();

        if(usersWhoDislikeSet == null) {
            usersWhoDislikeSet = new HashSet<>();
        }
        usersWhoDislikeSet.remove(user.getId());
        comment.setUsersWhoDislike(usersWhoDislikeSet);
        return comment;
    }
}
