package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByOrderByCreationDateDesc();

    void deleteById(String id);
}
