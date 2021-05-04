package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.web.request.FacebookLoginRequest;
import kjm.linkverifier.auth.security.jwtToken.JwtUtils;
import kjm.linkverifier.auth.security.services.FacebookService;
import kjm.linkverifier.auth.security.services.UserDetailsImpl;
import kjm.linkverifier.auth.web.response.TokenResponse;
import kjm.linkverifier.auth.web.request.LoginRequest;
import kjm.linkverifier.auth.web.response.InformationResponse;
import kjm.linkverifier.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/auth")
public class SignInController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final FacebookService facebookService;

    @Autowired
    public SignInController(UserService userService,
                            JwtUtils jwtUtils,
                            AuthenticationManager authenticationManager,
                            FacebookService facebookService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.facebookService = facebookService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        if(!userService.existsByEmail(loginRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new InformationResponse("Error: Wrong email"));
        }
        User user = userService.findByEmail(loginRequest.getEmail());
        if(!user.isConfirmed()) {
            return ResponseEntity
                    .badRequest()
                    .body(new InformationResponse("Error: Account is not confirmed"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);
        log.info("succesfuly logged in : {}", ((UserDetailsImpl)principal).getEmail());

        return ResponseEntity.ok(new TokenResponse(jwt));
    }

    @PostMapping("/facebook/signin")
    public  ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        User user = facebookService.getUserFromFacebook(facebookLoginRequest.getAccessToken(), facebookLoginRequest.getCreationDate());
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails2, null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String jwt = jwtUtils.generateJwtToken(authentication);
        log.info("Authenticate: " +((UserDetails)principal).getUsername());
        log.info("succesfuly logged in : {}", ((UserDetailsImpl)principal).getEmail());
        return ResponseEntity.ok(new TokenResponse(jwt));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return ResponseEntity.ok(new InformationResponse("Logout successful"));
    }
}
