package kjm.linkverifier.auth.mail;

import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MailVerificationTokenRepository extends MongoRepository<MailVerificationToken, String> {
    Optional<MailVerificationToken> findByUserAndTokenAndTokenType(User user, String token, TokenType tokenType);
    Optional<MailVerificationToken> findByUserAndTokenType(User user, TokenType tokenType);
}
