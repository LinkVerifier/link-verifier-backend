package kjm.linkverifier.auth.mail;

import kjm.linkverifier.auth.mail.exceptions.TokenException;
import kjm.linkverifier.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MailVerificationService {

    private final MailVerificationTokenRepository tokenRepository;

    public MailVerificationService(MailVerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public MailVerificationToken findByUserAndTokenAndTokenType(User user, String token, TokenType tokenType) {
        MailVerificationToken mailToken = tokenRepository.findByUserAndTokenAndTokenType(user, token, tokenType)
                .orElseThrow(() -> new TokenException("Error: Token is not found"));
        if (mailToken.getExpiryDate().before(new Date())) {
            throw new TokenException("Token expired");
        }
        return mailToken;
    }

    public void delete(MailVerificationToken token) {
        tokenRepository.delete(token);
    }

    public MailVerificationToken save(MailVerificationToken token) {
        return tokenRepository.save(token);
    }
}
