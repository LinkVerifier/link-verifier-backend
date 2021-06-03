package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.service.CurrentUser;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.auth.web.request.PasswordRequest;
import kjm.linkverifier.auth.web.request.UsernameRequest;
import kjm.linkverifier.auth.web.response.ExceptionResponse;
import kjm.linkverifier.auth.web.response.InformationResponse;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
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

    private final UserService userService;

    private final CommentService commentService;

    private final LinkService linkService;

    public UserController(PasswordEncoder passwordEncoder,
                          UserService userService,
                          CommentService commentService,
                          LinkService linkService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.commentService = commentService;
        this.linkService = linkService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void delete(@PathVariable("id") String id) {
        User user = userService.findById(id);
        userService.deleteUser(userService.findById(id));
        linkService.deleteCommentsAndSetRatingByComments(user.getComments());
    }

    @PutMapping("/change_username")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changeUsername(HttpServletRequest httpServletRequest,
                                            @Valid @RequestBody UsernameRequest usernameRequest) {
        User user = CurrentUser.getCurrentUser(httpServletRequest);
        user.setUsername(usernameRequest.getUsername());
        userService.save(user);
        return new ResponseEntity<>(new InformationResponse("Nazwa użytkownika zmieniona"), HttpStatus.OK);
    }

    @PutMapping("/change_password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(HttpServletRequest httpServletRequest,
                                            @Valid @RequestBody PasswordRequest passwordRequest) {

        User user = CurrentUser.getCurrentUser(httpServletRequest);
        if(!passwordRequest.getNewPassword().equals(passwordRequest.getNewRepeatedPassword())) {
            return new ResponseEntity<>(new ExceptionResponse("Hasła nie są takie same"), HttpStatus.OK);
        }
        if(passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        }
        userService.save(user);
        return new ResponseEntity<>("Hasło zmienione.", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getUserByCommentId(@RequestParam(name = "commentId", required = false) String commentId) {
        if(commentId == null) {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        }
        Comment comment = commentService.findById(commentId);
        return new ResponseEntity<>(userService.findByCommentsContaining(comment), HttpStatus.OK);
    }

    @GetMapping("/get_user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public User getCurrentUser(HttpServletRequest request){
        return CurrentUser.getCurrentUser(request);
    }

}
