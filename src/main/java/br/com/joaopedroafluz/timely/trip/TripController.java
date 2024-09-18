package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.activity.*;
import br.com.joaopedroafluz.timely.tripParticipant.TripParticipant;
import br.com.joaopedroafluz.timely.tripParticipant.TripParticipantService;
import br.com.joaopedroafluz.timely.user.User;
import br.com.joaopedroafluz.timely.user.UserDTO;
import br.com.joaopedroafluz.timely.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final UserService userService;
    private final ActivityService activityService;
    private final TripParticipantService tripParticipantService;

    // Trips
    @GetMapping("/{tripCode}")
    public ResponseEntity<TripResponseDTO> findTripByCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var owner = trip.get().getOwner();

        var ownerDTO = new UserDTO(owner.getCode(), owner.getName(), owner.getEmail());

        var tripResponseDTO = new TripResponseDTO(trip.get().getCode(), ownerDTO, trip.get().getDestination(),
                trip.get().getStartsAt(), trip.get().getEndsAt(), trip.get().getCreatedAt(),
                trip.get().getConfirmedAt());

        return ResponseEntity.ok().body(tripResponseDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody NewTripRequestDTO payload) {
        var owner = userService.findByCode(payload.ownerCode());
        if (owner.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var newTrip = Trip.builder()
                .code(UUID.randomUUID())
                .owner(owner.get())
                .destination(payload.destination())
                .startsAt(LocalDateTime.parse(payload.startsAt()))
                .endsAt(LocalDateTime.parse(payload.endsAt()))
                .build();

        var registeredUsers = userService.findAllByEmail(payload.participantsEmail());

        var newUsers = payload.participantsEmail().stream()
                .filter(email -> registeredUsers.stream()
                        .noneMatch(user -> user.getEmail().equals(email)))
                .map(email -> User.builder()
                        .code(UUID.randomUUID())
                        .email(email)
                        .build())
                .toList();

        userService.saveAll(newUsers);

        var allUsers = new ArrayList<>(registeredUsers);
        allUsers.addAll(newUsers);

        List<TripParticipant> participants = allUsers.stream()
                .filter(user -> payload.participantsEmail().contains(user.getEmail()))
                .map(user -> TripParticipant.builder()
                        .user(user)
                        .trip(newTrip)
                        .build())
                .toList();

        newTrip.setParticipants(participants);

        var persistedTrip = tripService.save(newTrip);

        return ResponseEntity.ok(new TripCreateResponse(persistedTrip.getCode()));
    }

    @PutMapping("/{tripCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateTrip(@PathVariable("tripCode") UUID tripCode,
                                           @RequestBody UpdatedTripRequestDTO payload) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        trip.get().setDestination(payload.destination());
        trip.get().setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
        trip.get().setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));

        tripService.save(trip.get());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tripCode}/confirm")
    public ResponseEntity<?> confirmTrip(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (trip.get().getConfirmedAt() != null) {
            return ResponseEntity.badRequest().body("Trip already confirmed");
        }

        trip.get().setConfirmedAt(LocalDateTime.now());

        tripService.save(trip.get());

        return ResponseEntity.noContent().build();
    }


    // Participants
    @GetMapping("/{tripCode}/participants")
    public ResponseEntity<List<UserDTO>> findParticipantsByTripCode(
            @PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var participantsByTripCode = userService.findAllByTripCode(tripCode);

        var participantsDTO = participantsByTripCode.stream()
                .map(participant -> new UserDTO(participant.getCode(), participant.getName(),
                        participant.getEmail()))
                .toList();

        return ResponseEntity.ok().body(participantsDTO);
    }

    @PostMapping("/{tripCode}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> inviteParticipants(@PathVariable UUID tripCode,
                                                @RequestBody NewParticipantRequestDTO payload) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        TripParticipant newParticipant;

        var userRegistered = userService.findByEmail(payload.email());

        if (userRegistered.isPresent()) {
            newParticipant = TripParticipant.builder()
                    .user(userRegistered.get())
                    .trip(trip.get())
                    .build();
        } else {
            var newUser = User.builder()
                    .code(UUID.randomUUID())
                    .email(payload.email())
                    .build();

            userService.save(newUser);

            newParticipant = TripParticipant.builder()
                    .user(newUser)
                    .trip(trip.get())
                    .build();
        }

        tripService.addParticipant(trip.get(), newParticipant);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tripCode}/participants/{participantCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> removeParticipant(@PathVariable("tripCode") UUID tripCode,
                                               @PathVariable("participantCode") UUID participantCode) {
        var tripParticipant = tripParticipantService.findByCode(tripCode, participantCode);

        if (tripParticipant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        tripParticipantService.remove(tripParticipant.get());

        return ResponseEntity.ok().build();
    }

    // Activities
    @GetMapping("/{tripCode}/activities")
    public ResponseEntity<List<ActivitiesResponseDTO>> findActivitiesByTripCode(@PathVariable("tripCode")
                                                                                UUID tripCode) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var activitiesByTripCode = activityService.findActivitiesByTripCode(tripCode);

        var activityResponseDTOS = activitiesByTripCode.stream()
                .map(activity -> new ActivityDTO(
                        activity.getTrip().getCode(),
                        activity.getCode(),
                        activity.getTitle(),
                        activity.getDescription(),
                        activity.getOccursAt()))
                .toList();

        var dates = getDatesBetween(trip.get().getStartsAt(), trip.get().getEndsAt());

        var activitiesResponseDTOS = dates.stream().map((date) -> (
                new ActivitiesResponseDTO(date,
                        activityResponseDTOS.stream()
                                .filter((activity) -> activity.occursAt().getDayOfYear() == date.getDayOfYear())
                                .sorted(Comparator.comparing(ActivityDTO::occursAt))
                                .collect(Collectors.toList()))
        )).collect(Collectors.toList());

        return ResponseEntity.ok().body(activitiesResponseDTOS);
    }

    @PostMapping("/{tripCode}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ActivityCreatedResponse> createActivity(@PathVariable UUID tripCode,
                                                                  @RequestBody ActivityRequestPayload payload) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var newActivity = Activity.builder()
                .trip(trip.get())
                .code(UUID.randomUUID())
                .title(payload.title())
                .description(payload.description())
                .occursAt(LocalDateTime.parse(payload.occursAt(), DateTimeFormatter.ISO_DATE_TIME))
                .build();

        var activityPersisted = activityService.save(newActivity);

        return ResponseEntity.ok(new ActivityCreatedResponse(activityPersisted.getCode()));
    }

    @DeleteMapping("/{tripCode}/activities/{activityCode}")
    public ResponseEntity<Void> removeActivity(@PathVariable("tripCode") UUID tripCode,
                                               @PathVariable("activityCode") UUID activityCode) {
        var trip = tripService.findByCode(tripCode);

        if (trip.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var activity = activityService.findByCode(activityCode);

        if (activity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        activityService.remove(activity.get());

        return ResponseEntity.noContent().build();
    }

    public static List<LocalDateTime> getDatesBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }

}
