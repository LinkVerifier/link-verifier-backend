package kjm.linkverifier.link.service;

import kjm.linkverifier.link.model.Link;
import kjm.linkverifier.link.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public Link save(Link link) {
        return linkRepository.save(link);
    }

    public boolean existsByLink(String link) {
        return linkRepository.existsByLink(link);
    }

    public Optional<Link> findByName(String link) {
        return linkRepository.findByLink(link);
    }

}
