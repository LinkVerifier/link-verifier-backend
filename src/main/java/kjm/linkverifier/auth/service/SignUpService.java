package kjm.linkverifier.auth.service;

import kjm.linkverifier.auth.mail.MailVerificationService;
import kjm.linkverifier.auth.mail.MailVerificationToken;
import kjm.linkverifier.auth.mail.TokenType;
import kjm.linkverifier.auth.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SignUpService {

    @Autowired
    private UserService userService;

    @Autowired
    private MailVerificationService verificationTokenService;


    public MailVerificationToken createMailVerificationToken(User user) {
        MailVerificationToken token = new MailVerificationToken(user, TokenType.SIGNUP);
        return verificationTokenService.save(token);
    }


    public User confirmSignUp(String email, String token) {
        User user = userService.findByEmail(email);
        MailVerificationToken t = verificationTokenService.findByUserAndTokenAndTokenType(user, token, TokenType.SIGNUP);
        user.setConfirmed(true);
        verificationTokenService.delete(t);
        return userService.save(user);
    }
}
