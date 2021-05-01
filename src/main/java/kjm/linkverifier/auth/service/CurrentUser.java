package kjm.linkverifier.auth.service;

import kjm.linkverifier.auth.exceptions.NotAuthenticatedException;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.security.jwtToken.AuthTokenFilter;
import kjm.linkverifier.auth.security.jwtToken.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
                new NotAuthenticatedException(jwtUtils.getUserEmailJwtToken(token)));
    }
}
