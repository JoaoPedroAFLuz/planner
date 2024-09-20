package br.com.joaopedroafluz.timely.link;

import br.com.joaopedroafluz.timely.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities/{activityCode}/links")
public class ActivityLinkController {

    private final LinkService linkService;
    private final LinkConverter linkConverter;
    private final ActivityService activityService;


    @GetMapping
    public List<LinkDTO> findLinksByTripCode(@PathVariable UUID activityCode) {
        activityService.findByCodeOrFail(activityCode);

        var linkPersisted = linkService.findAllByActivityCode(activityCode);

        return linkPersisted.stream()
                .map(linkConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerLink(@PathVariable UUID activityCode, @RequestBody NewLinkDTO newLinkDTO) {
        var activity = activityService.findByCodeOrFail(activityCode);

        var newLink = Link.builder()
                .activity(activity)
                .code(UUID.randomUUID())
                .title(newLinkDTO.title())
                .url(newLinkDTO.url())
                .build();

        linkService.save(newLink);
    }

    @PutMapping("/{linkCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLink(@PathVariable("activityCode") UUID activityCode, @PathVariable("linkCode") UUID linkCode,
                           @RequestBody UpdatedLinkDTO updatedLinkDTO) {
        var link = linkService.findByCodeAndActivityCode(linkCode, activityCode);

        link.setTitle(updatedLinkDTO.title());
        link.setUrl(updatedLinkDTO.url());

        linkService.save(link);
    }

    @DeleteMapping("/{linkCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLink(@PathVariable("activityCode") UUID activityCode, @PathVariable("linkCode") UUID linkCode) {
        linkService.removeByActivityCodeAndLinkCode(activityCode, linkCode);
    }

}
