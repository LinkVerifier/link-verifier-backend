package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends MongoRepository<Link, String> {
    boolean existsByLinkName(String link);

    Optional<Link> findByLinkName(String link);

    Optional<Link> findByCommentsContaining(Comment comment);

    List<Link> findAllByOrderByCreationDateDesc();

    List<Link> findAllByOrderByLastVisitDateDesc();
}
