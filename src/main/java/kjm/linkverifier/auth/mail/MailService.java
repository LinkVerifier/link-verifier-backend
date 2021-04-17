package kjm.linkverifier.auth.mail;

import kjm.linkverifier.auth.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;


@Component
@Slf4j
public class MailService {

    private final String CONTEXT = "http://localhost:8080/";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private void createRegistrationEmail(String token, User user) throws MessagingException {
        String url = "http://localhost:8080/auth/signup/confirm?userId=" +
                user.getId() + "&token=" + token;
        createEmail(url, user,"LinkVerifier - Activation link \n");
    }


    private void createEmail(String content,User user, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        context.setVariable("header","Dziękujemy za dołączenie do LinkVerifier");
        context.setVariable("title", "Kliknij w link, aby potwierdzić rejestrację ");
        context.setVariable("description", content);
        String body = templateEngine.process("mail", context);
        helper.setText(body, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        mailSender.send(message);
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setSubject(subject);
//        email.setText(content);
//        email.setTo(user.getEmail());
    }

    public void sendRegistrationEmail(String token, User user) throws MessagingException {
        createRegistrationEmail(token, user);
    }

}
