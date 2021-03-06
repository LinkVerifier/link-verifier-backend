package kjm.linkverifier.auth.mail.model;

import kjm.linkverifier.auth.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Data
@Document(collection = "verification_token")
@AllArgsConstructor
@NoArgsConstructor
public class MailVerificationToken {
    private static final int EXPIRATION = 60 * 24;

    @Id
    private String id;

    private String token;

    private TokenType tokenType;

    @DBRef
    private User user;

    private Date expiryDate;

    public MailVerificationToken(User user, TokenType tokenType) {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.expiryDate = calculateExpiryDate();
        this.tokenType = tokenType;
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
    public void updateToken() {
        this.expiryDate = calculateExpiryDate();
    }


}
