package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.RoleRepository;
import kjm.linkverifier.auth.request.FacebookLoginRequest;
import kjm.linkverifier.auth.security.jwtToken.JwtUtils;
import kjm.linkverifier.auth.security.services.FacebookService;
import kjm.linkverifier.auth.security.services.UserDetailsImpl;
import kjm.linkverifier.auth.response.TokenResponse;
import kjm.linkverifier.auth.request.LoginRequest;
import kjm.linkverifier.auth.response.InformationResponse;
import kjm.linkverifier.auth.request.RegisterRequest;
import kjm.linkverifier.auth.service.SignUpService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private PasswordEncoder encoder;
    private RoleRepository roleRepository;
    private UserService userService;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private FacebookService facebookService;
    private SignUpService signUpService;

    @Autowired
    public AuthController(PasswordEncoder encoder, RoleRepository roleRepository,
                          UserService userService, JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager, FacebookService facebookService,
                          SignUpService signUpService) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.facebookService = facebookService;
        this.signUpService = signUpService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
//        if(!user.isConfirmed()) {
//            throw new RuntimeException("Error: Account is not activated.");
//        }
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
