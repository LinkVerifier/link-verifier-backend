package kjm.linkverifier.auth.service;

import kjm.linkverifier.auth.exceptions.UserIsNotFoundException;
import kjm.linkverifier.auth.exceptions.WrongEmailException;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(WrongEmailException::new);
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserIsNotFoundException(id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User updateLikesInComment(Comment comment, Comment newComment) {
        User user = userRepository.findByCommentsContaining(comment)
                .orElseThrow(() -> new RuntimeException("Error: User is not found"));

        List<Comment> comments = user.getComments();
        if(comments == null) {
            comments = new ArrayList<>();
        }
        comments.remove(comment);
        comments.add(newComment);
        user.setComments(comments);
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
