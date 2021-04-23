package kjm.linkverifier.link.service;

import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.Opinion;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public Link findById(String id) {
        return linkRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: Link is not found"));
    }

    public Link save(Link link) {
        return linkRepository.save(link);
    }

    public boolean existsByLink(String link) {
        return linkRepository.existsByLinkName(link);
    }

    public Link findByName(String link) {
        return linkRepository.findByLinkName(link)
                .orElseThrow(() -> new RuntimeException("Error: Link is not found"));
    }

    public Link updateLikesInComment(Comment comment, Comment newComment) {
        Link link = linkRepository.findByCommentsContaining(comment)
                .orElseThrow(() -> new RuntimeException("Error: Link is not found"));

        List<Comment> comments = link.getComments();
        if(comments == null) {
            comments = new ArrayList<>();
        }
        comments.remove(comment);
        comments.add(newComment);
        link.setComments(comments);
        return linkRepository.save(link);
    }

    public String cleanURL(String url) {
        url = url.replaceFirst("^(http[s]?://www\\.|http[s]?://|www\\.)","");
        if(!url.matches("^youtube.*")) {
            String[] s = url.split("[?]");
            url = s[0];
        }
        return url;
    }

    public int calculateRatings(List<Comment> comments) {
        int counter = 0;
        for (Comment comm : comments) {
            if(comm.getOpinion().getName().equals(OpinionEnum.RELIABLE) ||
                    comm.getOpinion().getName().equals(OpinionEnum.SAFE) ||
                    comm.getOpinion().getName().equals(OpinionEnum.NEUTRAL)) {
                counter++;
            }
        }

        return (counter*100/comments.size());
    }

    public List<Link> findTopByOrderByIdDesc(int from, int to) {
        log.info("from {}, to {} ", from, to);
        return linkRepository.findAllByOrderByLastVisitDate().subList(from, to);
    }

    public List<Link> findAll() {
        return linkRepository.findAll();
    }

    public List<Link> findAllByOrderByLastVisitDate() {
        return linkRepository.findAllByOrderByLastVisitDate();
    }

}
