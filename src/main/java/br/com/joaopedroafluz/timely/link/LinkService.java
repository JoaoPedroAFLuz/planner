package br.com.joaopedroafluz.timely.link;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.joaopedroafluz.timely.auth.AuthorizationUtils.throwExceptionIfUserDoesNotHavePermission;

@Service
@AllArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;


    public Link findByCodeAndActivityCode(UUID linkCode, UUID activityCode) {
        var link = linkRepository.findByCodeAndActivityCode(linkCode, activityCode)
                .orElseThrow(LinkNotFoundException::new);

        throwExceptionIfUserDoesNotHavePermission(link.getActivity().getTrip());

        return link;
    }

    public List<Link> findAllByActivityCode(UUID activityCode) {
        var links = linkRepository.findAllByActivityCode(activityCode);

        throwExceptionIfUserDoesNotHavePermission(links.get(0).getActivity().getTrip());

        return links;
    }

    public void save(Link link) {
        throwExceptionIfUserDoesNotHavePermission(link.getActivity().getTrip());

        linkRepository.save(link);
    }

    public void removeByActivityCodeAndLinkCode(UUID activityCode, UUID linkCode) {
        var link = findByCodeAndActivityCode(linkCode, activityCode);

        throwExceptionIfUserDoesNotHavePermission(link.getActivity().getTrip());

        linkRepository.delete(link);
    }

}
