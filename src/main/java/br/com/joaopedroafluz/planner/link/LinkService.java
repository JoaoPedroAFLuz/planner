package br.com.joaopedroafluz.planner.link;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public Optional<Link> findByCode(UUID linkCode) {
        return linkRepository.findByCode(linkCode);
    }

    public List<Link> findAllByActivityCode(UUID tripCode) {
        return linkRepository.findAllByActivityCode(tripCode);
    }

    public Link save(Link link) {
        return linkRepository.save(link);
    }

    public void remove(Link link) {
        linkRepository.delete(link);
    }

}
