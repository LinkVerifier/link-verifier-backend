package kjm.linkverifier.auth.service;

import kjm.linkverifier.auth.mail.*;
import kjm.linkverifier.auth.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
@Slf4j
public class SignUpService {

    @Autowired
    private UserService userService;

    @Autowired
    private MailVerificationService verificationTokenService;

    @Autowired
    private MailVerificationTokenRepository tokenRepository;

    @Autowired
    private MailService mailService;


    public User confirmSignUp(String id, String token) {
        User user = userService.findById(id);
        MailVerificationToken tokenToDelete = verificationTokenService.findByUserAndTokenAndTokenType(user, token, TokenType.SIGNUP);
        user.setConfirmed(true);
        verificationTokenService.delete(tokenToDelete);
        return userService.save(user);
    }

    public void resendVerificationToken(String email) throws MessagingException {
        User user = userService.findByEmail(email);
        Optional<MailVerificationToken> t = tokenRepository.findByUserAndTokenType(user,TokenType.SIGNUP);
        MailVerificationToken token;
        if (t.isPresent()) {
            token = t.get();
            token.updateToken();
            tokenRepository.save(token);
        } else
            token = createVerificationToken(user);
        mailService.sendRegistrationEmail(token.getToken(), user);
    }

    public MailVerificationToken createVerificationToken(User user) {
        MailVerificationToken token = new MailVerificationToken(user,TokenType.SIGNUP);
        return tokenRepository.save(token);
    }

}
