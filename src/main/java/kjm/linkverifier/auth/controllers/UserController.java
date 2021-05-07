package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.exceptions.PasswordsNotMatchException;
import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.auth.web.request.PasswordRequest;
import kjm.linkverifier.auth.web.request.UsernameRequest;
import kjm.linkverifier.auth.web.response.ExceptionResponse;
import kjm.linkverifier.auth.web.response.InformationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException(id));
    }

    @PutMapping("/change_username")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changeUsername(HttpServletRequest httpServletRequest,
                                            @Valid @RequestBody UsernameRequest usernameRequest) {
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        user.setUsername(usernameRequest.getUsername());
        userRepository.save(user);
        return new ResponseEntity<>(new InformationResponse("Nazwa użytkownika zmieniona"), HttpStatus.OK);
    }

    @PutMapping("/change_password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(HttpServletRequest httpServletRequest,
                                            @Valid @RequestBody PasswordRequest passwordRequest) {

        User user = CurrentUser.getCurrentUser(httpServletRequest);
        if(!passwordRequest.getNewPassword().equals(passwordRequest.getNewRepeatedPassword())) {
            return new ResponseEntity<>(new ExceptionResponse("Hasła nie są takie same"), HttpStatus.OK);
            //throw new PasswordsNotMatchException("Passwords do not match.");
        }
        if(passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        }
        userRepository.save(user);
        return new ResponseEntity<>("Hasło zmienione.", HttpStatus.OK);
    }

    @GetMapping("/get_user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getCurrentUser(HttpServletRequest request){
        return CurrentUser.getCurrentUser(request);
    }

}
