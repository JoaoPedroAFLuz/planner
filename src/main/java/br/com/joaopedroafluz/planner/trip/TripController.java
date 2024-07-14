package br.com.joaopedroafluz.planner.trip;

import br.com.joaopedroafluz.planner.participant.ParticipantService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/trips")
@AllArgsConstructor
public class TripController {

    private final TripRepository tripRepository;
    private final ParticipantService participantService;

    @GetMapping("/{code}")
    public ResponseEntity<Trip> findTripByCode(@PathVariable("code") UUID code) {
        var trip = tripRepository.findByCode(code);

        return trip.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        tripRepository.save(newTrip);

        participantService.registerParticipantsTotEvent(payload.emailsToInvite(), newTrip.getCode());
        participantService.triggerConfirmationEmailToParticipant(newTrip.getCode());

        return ResponseEntity.ok(new TripCreateResponse(newTrip.getCode()));
    }

}
