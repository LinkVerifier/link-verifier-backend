package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.RoleRepository;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.request.FacebookLoginRequest;
import kjm.linkverifier.auth.security.jwtToken.JwtUtils;
import kjm.linkverifier.auth.security.services.FacebookService;
import kjm.linkverifier.auth.security.services.UserDetailsImpl;
import kjm.linkverifier.auth.response.TokenResponse;
import kjm.linkverifier.auth.request.LoginRequest;
import kjm.linkverifier.auth.response.InformationResponse;
import kjm.linkverifier.auth.request.RegisterRequest;
import kjm.linkverifier.auth.security.services.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    private PasswordEncoder encoder;
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private FacebookService facebookService;

    public AuthController(PasswordEncoder encoder, RoleRepository roleRepository,
                          UserRepository userRepository, JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager, FacebookService facebookService) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.facebookService = facebookService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(authentication);
        log.info("succesfuly logged in : {}", ((UserDetailsImpl)principal).getEmail());

        return ResponseEntity.ok(new TokenResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userRepository.findByEmail(userDetails.getEmail()).get().getProfilePicture(),
                userDetails.getEmail(),
                rolesList));
    }

    @PostMapping("/facebook/signin")
    public  ResponseEntity<?> facebookAuth(@Valid @RequestBody FacebookLoginRequest facebookLoginRequest) {
        User user = facebookService.getUserFromFacebook(facebookLoginRequest.getAccessToken());
        UserDetailsImpl userDetails2 = UserDetailsImpl.build(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails2, null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwt = jwtUtils.generateJwtToken(authentication);
        log.info("Authenticate: " +((UserDetails)principal).getUsername());
        log.info("succesfuly logged in : {}", ((UserDetailsImpl)principal).getEmail());
        return ResponseEntity.ok(new TokenResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userRepository.findByEmail(userDetails.getEmail()).get().getProfilePicture(),
                userDetails.getEmail(),
                rolesList));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest registerRequest) {
    if(userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new InformationResponse("Error: Email is already in use!"));
        }

        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()),
                "https://cdn.pixabay.com/photo/2016/10/26/22/00/hamster-1772742_960_720.jpg",
                new Date(registerRequest.getCreationDate()));

        Date date = new Date(registerRequest.getCreationDate());
        Set<String> strRoles = registerRequest.getRoles();
        Set<Role> rolesToSet = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            rolesToSet.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if(role.equals("admin")) {
                    Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    rolesToSet.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    rolesToSet.add(userRole);
                }
            });
        }

        user.setRoles(rolesToSet);
        userRepository.save(user);

        return ResponseEntity.ok(new InformationResponse("User registered successfully!"));
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> logoutUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return ResponseEntity.ok(new InformationResponse("Logout successful"));
    }
}
