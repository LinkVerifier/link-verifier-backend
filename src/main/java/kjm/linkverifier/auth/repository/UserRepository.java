package kjm.linkverifier.auth.repository;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.link.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByCommentsContaining(Comment comment);

}
