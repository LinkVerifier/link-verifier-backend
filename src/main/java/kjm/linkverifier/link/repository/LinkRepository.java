package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepository extends MongoRepository<Link, String> {
    boolean existsByLinkName(String link);

    Optional<Link> findByLinkName(String link);

    List<Link> findAllByOrderByCreationDateDesc();

    List<Link> findAllByOrderByRatingAsc();

    List<Link> findAllByOrderByViewsDesc();

    Optional<Link> findLinkByCommentsLike(Comment comment);

    Optional<Link> findByCommentsContaining(Comment comment);

}
