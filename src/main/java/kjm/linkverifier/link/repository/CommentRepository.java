package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    @Aggregation("{ $sort: { $size: '$usersWhoLike' }: -1, $creationDate : 1  }")
    List<Comment> sortAllCommentsByUsersWhoLike();


}
