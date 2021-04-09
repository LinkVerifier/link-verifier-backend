package kjm.linkverifier.link.controllers;

import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.CommentRepository;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.repository.OpinionRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    LinkRepository linkRepository;

    @PostMapping("/{id}")
    public Link addComment(@PathVariable("id") String id, @RequestBody CommentRequest commentRequest) {
        Link link = linkRepository.findById(id).orElseThrow(() -> new RuntimeException("Error : Link does not exist"));
        log.info(commentRequest.getComment(), commentRequest.getOpinion(), commentRequest.getDate());
        Date date = new Date(commentRequest.getDate());
        String opinionStr = commentRequest.getOpinion();
        Opinion opinion;

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
        Comment comment = new Comment(commentRequest.getComment(), date, opinion);
        List<Comment> commentList = new ArrayList<>();
        List<Opinion> opinions = new ArrayList<>();
        if(link.getComments() != null) {
            commentList = link.getComments();
        } else if (link.getOpinions() != null) {
            opinions = link.getOpinions();
        }
        commentList.add(comment);
        link.setComments(commentList);
        opinions.add(comment.getOpinion());
        link.setOpinions(opinions);
        commentRepository.save(comment);
        return linkRepository.save(link);
    }


    @GetMapping("/{id}")
    public Link getLinkDetails(@PathVariable("id") String id) {
        return linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error : Link does not exist"));
    }
}
