package kjm.linkverifier.link.controllers;

import kjm.linkverifier.auth.models.User;
import kjm.linkverifier.auth.repository.UserRepository;
import kjm.linkverifier.auth.security.services.UserDetailsImpl;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import kjm.linkverifier.link.service.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/links")
@Slf4j
public class LinkController {

    @Autowired
    OpinionRepository opinionRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LinkService linkService;

    @PostMapping("/{id}")
    public Link addComment(@PathVariable("id") String id, @RequestBody CommentRequest commentRequest) {
        Link link = linkService.findById(id);
        log.info(commentRequest.getComment(), commentRequest.getOpinion(), commentRequest.getDate());
        Date date = new Date(commentRequest.getDate());
        String opinionStr = commentRequest.getOpinion();
        Opinion opinion;
        String email = ((UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Error: User not found"));

        if(opinionStr == null) {
            opinion = opinionRepository.findByName(OpinionEnum.NEUTRAL)
                    .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
        } else {
            switch (opinionStr) {
                case "VIRUS":
                    opinion = opinionRepository.findByName(OpinionEnum.VIRUS)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "FAKE_NEWS":
                    opinion = opinionRepository.findByName(OpinionEnum.FAKE_NEWS)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "FRAUD":
                    opinion = opinionRepository.findByName(OpinionEnum.FRAUD)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "INDECENT_CONTENT":
                    opinion = opinionRepository.findByName(OpinionEnum.INDECENT_CONTENT)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "SAFE":
                    opinion = opinionRepository.findByName(OpinionEnum.SAFE)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                case "RELIABLE":
                    opinion = opinionRepository.findByName(OpinionEnum.RELIABLE)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
                    break;
                default:
                    opinion = opinionRepository.findByName(OpinionEnum.NEUTRAL)
                            .orElseThrow(() -> new RuntimeException("Error : Opinion is not found"));
            }
        }
        Comment comment = new Comment(commentRequest.getComment(), user.getId(), date, opinion);
        List<Comment> commentLinkList = link.getComments();
        List<Comment> commentUserList = user.getComments();
        if(commentLinkList == null) {
            commentLinkList = new ArrayList<>();
        }
        if (commentUserList == null) {
            commentUserList = new ArrayList<>();
        }
        commentLinkList.add(comment);
        commentUserList.add(comment);
        link.setComments(commentLinkList);
        user.setComments(commentUserList);
        commentRepository.save(comment);
        userRepository.save(user);
        return linkService.save(link);
    }


    @GetMapping("/{id}")
    public Link getLinkDetails(@PathVariable("id") String id) {
        return linkService.findById(id);
    }
}
