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

    private static UserRepository userRepository;

    private static AuthTokenFilter tokenAuthenticationFilter;

    private static JwtUtils jwtUtils;

    public CurrentUser(UserRepository userRepository,
                       AuthTokenFilter tokenAuthenticationFilter,
                       JwtUtils jwtUtils) {
        CurrentUser.userRepository = userRepository;
        CurrentUser.jwtUtils = jwtUtils;
        CurrentUser.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    public static User getCurrentUser(HttpServletRequest request){
        String token = tokenAuthenticationFilter.parseJwt(request);
        return userRepository.findByEmail(jwtUtils.getUserEmailJwtToken(token)).orElseThrow(() ->
                new RuntimeException("Error: User with email: " + jwtUtils.getUserEmailJwtToken(token) + " is not authenticated"));
    }
}
