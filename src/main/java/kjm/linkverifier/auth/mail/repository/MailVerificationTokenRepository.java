package kjm.linkverifier.auth.mail.repository;


import kjm.linkverifier.auth.mail.model.MailVerificationToken;
import kjm.linkverifier.auth.mail.model.TokenType;
import kjm.linkverifier.auth.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailVerificationTokenRepository extends MongoRepository<MailVerificationToken, String> {

    Optional<MailVerificationToken> findByUserAndTokenAndTokenType(User user, String token, TokenType tokenType);

    Optional<MailVerificationToken> findByUserAndTokenType(User user, TokenType tokenType);
}
