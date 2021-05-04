package kjm.linkverifier.link.service;

import kjm.linkverifier.link.exceptions.LinkNotFoundException;
import kjm.linkverifier.link.model.Comment;
import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.model.OpinionEnum;
import kjm.linkverifier.link.repository.LinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void addViews(String id) {
        Link link = findById(id);
        link.setViews(link.getViews() + 1);
        linkRepository.save(link);
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
        if(comments.size() == 0) {
            return 0;
        }
        int counter = 0;
        for (Comment comm : comments) {
            if(comm.getOpinion().getName().equals(OpinionEnum.FRAUD) ||
                    comm.getOpinion().getName().equals(OpinionEnum.INDECENT_CONTENT) ||
                    comm.getOpinion().getName().equals(OpinionEnum.FAKE_NEWS) ||
                    comm.getOpinion().getName().equals(OpinionEnum.VIRUS)) {
                counter++;
            }
        }
        comments.removeIf(s -> s.getOpinion().getName().equals(OpinionEnum.NEUTRAL));
        if(comments.size() == 0) {
            return 0;
        }
        return (counter*100/comments.size());
    }

    public Link findLinkByCommentsLike(Comment comment) {
        return linkRepository.findLinkByCommentsLike(comment)
                .orElseThrow(LinkNotFoundException::new);
    }

    public List<Link> findAllByOrderByCreationDateDesc() {
        return linkRepository.findAllByOrderByCreationDateDesc();
    }

    public List<Link> findAllByOrderByCreationDateDesc(int from, int to) {
        if(linkRepository.findAllByOrderByCreationDateDesc().size()<to) {
            return linkRepository.findAllByOrderByCreationDateDesc();
        }
        return linkRepository.findAllByOrderByCreationDateDesc().subList(from,to);
    }

    public List<Link> findAll() {
        return linkRepository.findAll();
    }

    public List<Link> findAllByOrderByRatingAsc() {
        return linkRepository.findAllByOrderByRatingAsc();
    }

    public List<Link> findAllByOrderByRatingAsc(int from, int to) {
        if(linkRepository.findAllByOrderByRatingAsc().size()<to) {
            return linkRepository.findAllByOrderByRatingAsc();
        }
        return linkRepository.findAllByOrderByRatingAsc().subList(from, to);
    }

    public List<Link> findAllByOrderByViewsDesc() {
        return linkRepository.findAllByOrderByViewsDesc();
    }

    public List<Link> findAllByOrderByViewsDesc(int from, int to) {
        if(linkRepository.findAllByOrderByViewsDesc().size() < to) {
            return linkRepository.findAllByOrderByViewsDesc();
        }
        return linkRepository.findAllByOrderByViewsDesc().subList(from, to);
    }

}
