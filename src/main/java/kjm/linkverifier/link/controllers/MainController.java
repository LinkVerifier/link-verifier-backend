package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.service.UserService;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.response.StatisticsResponse;
import kjm.linkverifier.link.service.CommentService;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
@Slf4j
public class MainController {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> showMainPage() {
        log.info("Showing main page");

        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("statistics")
    public StatisticsResponse getStatistics() {
        int users = userRepository.findAll().size();
        int links = linkRepository.findAll().size();
        int comments = commentRepository.findAll().size();
        return new StatisticsResponse(users,links,comments);
    }

}
