package kjm.linkverifier.auth.repository;

import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(RoleEnum roleName);
}
