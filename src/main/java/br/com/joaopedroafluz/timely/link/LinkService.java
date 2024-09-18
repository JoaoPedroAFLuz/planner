package br.com.joaopedroafluz.timely.link;

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

    public List<Link> findAllByActivityCode(UUID activityCode) {
        return linkRepository.findAllByActivityCode(activityCode);
    }

    public void save(Link link) {
        linkRepository.save(link);
    }

    public void removeByActivityCodeAndLinkCode(UUID activityCode, UUID linkCode) {
        var link = findByCode(linkCode).orElseThrow(LinkNotFoundException::new);

        if (!link.getActivity().getCode().equals(activityCode)) {
            throw new InvalidLinkRequestException("Link não pertence à atividade informada");
        }

        linkRepository.delete(link);
    }

}
