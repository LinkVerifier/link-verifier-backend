package kjm.linkverifier.auth.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.service.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> findUser(@PathVariable("id") String id) {
        log.info("getting user {}", userRepository.findById(id));

        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException(id));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> changeDetails(@PathVariable("id") String id) {
//        log.info("getting user {}", userRepository.findById(id));
//
//        return userRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElseThrow(() -> new RuntimeException(id));
//    }



    @GetMapping("/get_user")
    public User getCurrentUser(HttpServletRequest request){
        return CurrentUser.getCurrentUser(request);
    }

}
