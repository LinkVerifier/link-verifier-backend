package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends MongoRepository<Link, String> {
    boolean existsByLinkName(String link);

    Optional<Link> findByLinkName(String link);

    List<Link> findAllByOrderByCreationDateDesc();

    List<Link> findAllByOrderByRatingAsc();

    List<Link> findAllByOrderByRatingDesc();

    List<Link> findAllByOrderByViewsDesc();

    Optional<Link> findLinkByCommentsLike(Comment comment);
}
