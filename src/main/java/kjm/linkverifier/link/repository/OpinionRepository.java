package kjm.linkverifier.link.repository;

import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpinionRepository extends MongoRepository<Opinion, String> {
    Optional<Opinion> findByName(OpinionEnum opinion);
}
