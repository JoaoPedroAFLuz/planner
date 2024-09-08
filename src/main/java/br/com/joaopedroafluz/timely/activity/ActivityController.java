package br.com.joaopedroafluz.timely.activity;

import br.com.joaopedroafluz.timely.link.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/activities")
public class ActivityController {

    private final LinkService linkService;
    private final ActivityService activityService;

    @GetMapping("/{activityCode}")
    public ResponseEntity<Activity> findByCode(@PathVariable("activityCode") UUID activityCode) {
        var activity = activityService.findByCode(activityCode);

        return activity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{activityCode}/links")
    public ResponseEntity<List<LinkResponseDTO>> findLinksByTripCode(@PathVariable UUID activityCode) {
        var activity = activityService.findByCode(activityCode);

        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var linkPersisted = linkService.findAllByActivityCode(activityCode);

        var linkResponseDTOs = linkPersisted.stream()
                .map(link -> new LinkResponseDTO(
                        link.getActivity().getCode(),
                        link.getCode(),
                        link.getTitle(),
                        link.getUrl()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(linkResponseDTOs);
    }

    @PostMapping("/{activityCode}/links")
    public ResponseEntity<LinkCreatedResponse> registerLink(@PathVariable UUID activityCode,
                                                            @RequestBody LinkRequestPayload payload) {
        var activity = activityService.findByCode(activityCode);

        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newLink = Link.builder()
                .activity(activity.get())
                .code(UUID.randomUUID())
                .title(payload.title())
                .url(payload.url())
                .build();

        var linkPersisted = linkService.save(newLink);

        return ResponseEntity.ok().body(new LinkCreatedResponse(linkPersisted.getCode()));
    }

    @DeleteMapping("/{activityCode}/links/{linkCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removeLink(@PathVariable("activityCode") UUID activityCode,
                                           @PathVariable("linkCode") UUID linkCode) {
        var activity = activityService.findByCode(activityCode);

        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var link = linkService.findByCode(linkCode);

        if (link.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        linkService.remove(link.get());

        return ResponseEntity.ok().build();
    }

}
