package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Link;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LinkRepository extends MongoRepository<Link, String> {
    boolean existsByLink(String link);
    Optional<Link> findByLink(String link);

}
