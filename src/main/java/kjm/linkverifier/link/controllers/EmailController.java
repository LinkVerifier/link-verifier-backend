package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.link.requests.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/emails")
public class EmailController {

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public boolean checkIfEmailExists(EmailRequest emailRequest) {
        return userRepository.existsByEmail(emailRequest.getEmail());
    }
    
}
