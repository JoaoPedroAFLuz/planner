package br.com.joaopedroafluz.planner.trip;

import br.com.joaopedroafluz.planner.activity.*;
import br.com.joaopedroafluz.planner.link.Link;
import br.com.joaopedroafluz.planner.link.LinkRequestPayload;
import br.com.joaopedroafluz.planner.link.LinkResponseDTO;
import br.com.joaopedroafluz.planner.link.LinkService;
import br.com.joaopedroafluz.planner.participant.ParticipantInvitedResponse;
import br.com.joaopedroafluz.planner.participant.ParticipantRequestPayload;
import br.com.joaopedroafluz.planner.participant.ParticipantResponseDTO;
import br.com.joaopedroafluz.planner.participant.ParticipantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trips")
@AllArgsConstructor
public class TripController {

    private final LinkService linkService;
    private final TripRepository tripRepository;
    private final ActivityService activityService;
    private final ParticipantService participantService;

    // Trips
    @GetMapping("/{tripCode}")
    public ResponseEntity<Trip> findTripByCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripRepository.findByCode(tripCode);

        return trip.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        var newTrip = new Trip(payload);

        tripRepository.save(newTrip);

        participantService.registerParticipantsTotEvent(payload.emailsToInvite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getCode()));
    }

    @PutMapping("/{tripCode}")
    public ResponseEntity<Trip> updateTrip(@PathVariable("tripCode") UUID tripCode,
                                           @RequestBody TripRequestPayload payload) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        trip.get().setOwnerName(payload.ownerName());
        trip.get().setOwnerEmail(payload.ownerEmail());
        trip.get().setDestination(payload.destination());
        trip.get().setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
        trip.get().setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));

        tripRepository.save(trip.get());

        return ResponseEntity.ok(trip.get());
    }

    @PatchMapping("/{tripCode}/confirm")
    public ResponseEntity<String> confirmTrip(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (trip.get().getConfirmedAt() != null) {
            return ResponseEntity.badRequest().body("Trip already confirmed");
        }

        trip.get().setConfirmedAt(LocalDateTime.now());

        tripRepository.save(trip.get());
        participantService.triggerConfirmationEmailToParticipants(trip.get().getCode());

        return ResponseEntity.ok().body("Trip confirmed");
    }


    // Participants
    @GetMapping("/{tripCode}/participants")
    public ResponseEntity<List<ParticipantResponseDTO>> findParticipantsByTripCode(
            @PathVariable("tripCode") UUID tripCode) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var participantsByTripCode = participantService.findParticipantsByTripCode(tripCode);

        var participants = participantsByTripCode.stream()
                .map(participant -> new ParticipantResponseDTO(
                        participant.getTrip().getCode(),
                        participant.getCode(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getConfirmedAt())
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(participants);
    }

    @PostMapping("/{tripCode}/invite")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ParticipantInvitedResponse> inviteParticipants(@PathVariable UUID tripCode,
                                                                         @RequestBody ParticipantRequestPayload payload) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var participant = participantService.registerParticipantTotEvent(payload.email(), trip.get());

        if (trip.get().getConfirmedAt() != null) {
            participantService.triggerConfirmationEmailToParticipant(payload.email());
        }

        return ResponseEntity.ok(new ParticipantInvitedResponse(participant.getCode()));
    }

    // Activities
    @GetMapping("/{tripCode}/activities")
    public ResponseEntity<List<ActivityResponseDTO>> findActivitiesByTripCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var activitiesByTripCode = activityService.findActivitiesByTripCode(tripCode);

        var activityResponseDTOS = activitiesByTripCode.stream()
                .map(activity -> new ActivityResponseDTO(
                        activity.getTrip().getCode(),
                        activity.getCode(),
                        activity.getTitle(),
                        activity.getOccursAt()))
                .toList();

        return ResponseEntity.ok().body(activityResponseDTOS);
    }

    @PostMapping("/{tripCode}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActivityCreatedResponse> createActivity(@PathVariable UUID tripCode,
                                                                  @RequestBody ActivityRequestPayload payload) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newActivity = Activity.builder()
                .trip(trip.get())
                .code(UUID.randomUUID())
                .title(payload.title())
                .occursAt(LocalDateTime.parse(payload.occursAt(), DateTimeFormatter.ISO_DATE_TIME))
                .build();

        var activityPersisted = activityService.save(newActivity);

        return ResponseEntity.ok(new ActivityCreatedResponse(activityPersisted.getCode()));
    }

    // Links
    @PostMapping("/{tripCode}/links")
    public ResponseEntity<LinkResponseDTO> registerLink(@PathVariable UUID tripCode,
                                                        @RequestBody LinkRequestPayload payload) {
        var trip = tripRepository.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newLink = Link.builder()
                .trip(trip.get())
                .code(UUID.randomUUID())
                .title(payload.title())
                .url(payload.url())
                .build();

        var linkPersisted = linkService.save(newLink);

        return ResponseEntity.ok().body(new LinkResponseDTO(linkPersisted.getCode()));
    }

}
