package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.net.CookieHandler;
import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    //@Aggregation("{ $sort: { $size: '$usersWhoLike' }: -1, $creationDate : 1  }")
    @Aggregation("{ $sort: { $creationDate : 1  }")
    List<Comment> sortAllCommentsByCreationDate();

    List<Comment> findAllByOrderByCreationDateDesc();

    @Query("{'links' :{'$ref' : 'company' , '$id' : ?0}}")
    List<Comment> find(String companyId);

    //List<Comment> findAllByLinkIdOrderByCreationDateDesc(String id);

    void deleteById(String id);

    //void deleteComments(List<Comment> comments);


}
