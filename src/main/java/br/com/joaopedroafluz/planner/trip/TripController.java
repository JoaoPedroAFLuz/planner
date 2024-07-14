package br.com.joaopedroafluz.planner.trip;

import br.com.joaopedroafluz.planner.participant.*;
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

    private final TripRepository tripRepository;
    private final ParticipantService participantService;

    @GetMapping("/{tripCode}")
    public ResponseEntity<Trip> findTripByCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripRepository.findByCode(tripCode);

        return trip.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

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
                        participant.getCode(),
                        participant.getTrip().getCode(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.getConfirmedAt())
                )
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(participants);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        var newTrip = new Trip(payload);

        tripRepository.save(newTrip);

        participantService.registerParticipantsTotEvent(payload.emailsToInvite(), newTrip);

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getCode()));
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

}
