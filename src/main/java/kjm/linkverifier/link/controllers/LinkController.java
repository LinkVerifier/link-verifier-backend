package kjm.linkverifier.link.controllers;

import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.repository.LinkRepository;
import kjm.linkverifier.link.requests.CommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/links")
public class LinkController {

    @Autowired
    LinkRepository linkRepository;

    @PostMapping("/{id}")
    public Link addComment(@PathVariable("id") String id, CommentRequest commentRequest) {
        Link link = linkRepository.findById(id).orElseThrow(() -> new RuntimeException("Error : Link does not exist"));
        Date date = new Date(commentRequest.getDate());
        String opinion = commentRequest.getOpinion();
        if(opinion.equals("")){}

        ///tu
        Comment comment = new Comment(commentRequest.getComment(), date, new Opinion("enumTU"));
        List<Comment> commentList = link.getComments();
        commentList.add(comment);
        link.setComments(commentList);
        List<Opinion> opinions = link.getOpinions();
        opinions.add(comment.getOpinion());
        link.setOpinions(opinions);
        return linkRepository.save(link);
    }


    @GetMapping("/{id}")
    public Link getLinkDetails(@PathVariable("id") String id) {
        return linkRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error : Link does not exist"));
    }
}
