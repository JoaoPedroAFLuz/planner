package br.com.joaopedroafluz.planner.link;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public Link save(Link link) {
        return linkRepository.save(link);
    }

}
