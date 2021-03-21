package kjm.linkverifier.auth.repository;

import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;


public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleEnum roleName);
}
