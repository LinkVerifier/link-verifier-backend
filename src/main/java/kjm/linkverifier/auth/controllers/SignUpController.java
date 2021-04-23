package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.mail.MailService;
import kjm.linkverifier.auth.models.Role;
import kjm.linkverifier.auth.models.RoleEnum;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.RoleRepository;
import kjm.linkverifier.auth.web.request.RegisterRequest;
import kjm.linkverifier.auth.web.response.InformationResponse;
import kjm.linkverifier.auth.service.SignUpService;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.files.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Slf4j
@RequestMapping("/auth")
public class SignUpController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SignUpService signUpService;

    @Autowired
    private MailService mailService;

    @Autowired
    private FileService fileService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest registerRequest) throws MessagingException {
        if(userService.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new InformationResponse("Error: Email is already in use!"));
        }

        User user = new User(registerRequest.getUsername(),
                registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()),
                new Date(registerRequest.getCreationDate()));

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
        java.io.File file = new File("profilepic.jpg");
        kjm.linkverifier.files.model.File savedFile = fileService.store(file);
        user.setProfilePicture(savedFile);
        userService.save(user);
        mailService.sendRegistrationEmail(signUpService.createVerificationToken(user).getToken(), user);
        return ResponseEntity.ok(new InformationResponse("User registered successfully!"));
    }

    @PutMapping("/signup/confirm")
    public User confirmRegistration(@RequestParam(name = "userId") String userId, @RequestParam(name = "token") String token) {
        log.info("id {}, token {}",userId, token);
        return signUpService.confirmSignUp(userId, token);
    }

    @GetMapping("/register/resend_confirmation")
    public void resendVerificationToken(@RequestParam String email) throws MessagingException {
        signUpService.resendVerificationToken(email);
    }

}
