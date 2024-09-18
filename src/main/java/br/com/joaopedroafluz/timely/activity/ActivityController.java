package br.com.joaopedroafluz.timely.activity;

import br.com.joaopedroafluz.timely.link.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/activities")
public class ActivityController {

    private final LinkService linkService;
    private final LinkConverter linkConverter;
    private final ActivityService activityService;
    private final ActivityConverter activityConverter;

    @GetMapping("/{activityCode}")
    public ActivityDTO findByCode(@PathVariable("activityCode") UUID activityCode) {
        var activity = activityService.findByCode(activityCode).orElseThrow(ActivityNotFoundException::new);

        return activityConverter.entityToDTO(activity);
    }

    @GetMapping("/{activityCode}/links")
    public List<LinkDTO> findLinksByTripCode(@PathVariable UUID activityCode) {
        activityService.findByCode(activityCode).orElseThrow(ActivityNotFoundException::new);

        var linkPersisted = linkService.findAllByActivityCode(activityCode);

        return linkPersisted.stream()
                .map(linkConverter::entityToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/{activityCode}/links")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerLink(@PathVariable UUID activityCode, @RequestBody NewLinkDTO newLinkDTO) {
        var activity = activityService.findByCode(activityCode).orElseThrow(ActivityNotFoundException::new);

        var newLink = Link.builder()
                .activity(activity)
                .code(UUID.randomUUID())
                .title(newLinkDTO.title())
                .url(newLinkDTO.url())
                .build();

        linkService.save(newLink);
    }

    @DeleteMapping("/{activityCode}/links/{linkCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLink(@PathVariable("activityCode") UUID activityCode, @PathVariable("linkCode") UUID linkCode) {
        linkService.removeByActivityCodeAndLinkCode(activityCode, linkCode);
    }

}
