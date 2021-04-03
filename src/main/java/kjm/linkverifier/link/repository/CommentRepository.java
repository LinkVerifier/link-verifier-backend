package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
