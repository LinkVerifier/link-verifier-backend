package kjm.linkverifier.auth.security.services;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.security.jwtToken.AuthTokenFilter;
import kjm.linkverifier.auth.security.jwtToken.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class CurrentUser {
    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static AuthTokenFilter tokenAuthenticationFilter;

    @Autowired
    private static JwtUtils jwtUtils;

    public static User getCurrentUser(HttpServletRequest request){
        log.info("XD: "+request.getHeader("Bearer "));
        String token = tokenAuthenticationFilter.parseJwt(request);
        return userRepository.findByEmail(jwtUtils.getUserEmailJwtToken(token)).orElseThrow(() ->
                new RuntimeException("Error: User with email: " + jwtUtils.getUserEmailJwtToken(token) + " is not authenticated"));
    }
}
