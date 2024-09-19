package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.activity.*;
import br.com.joaopedroafluz.timely.tripParticipant.TripParticipant;
import br.com.joaopedroafluz.timely.tripParticipant.TripParticipantService;
import br.com.joaopedroafluz.timely.user.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final UserService userService;
    private final UserConverter userConverter;
    private final TripConverter tripConverter;
    private final ActivityService activityService;
    private final ActivityConverter activityConverter;
    private final TripParticipantService tripParticipantService;

    // Trips
    @GetMapping("/{tripCode}")
    public TripResponseDTO findTripByCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCode(tripCode).orElseThrow(TripNotFoundException::new);
        var owner = trip.getOwner();
        var ownerDTO = userConverter.entityToDTO(owner);

        return tripConverter.entityToDTO(trip, ownerDTO);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripCodeDTO createTrip(@RequestBody NewTripDTO newTripDTO) {
        var owner = userService.findByCode(newTripDTO.ownerCode()).orElseThrow(UserNotFoundException::new);

        var startAt = LocalDateTime.parse(newTripDTO.startsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        var endAt = LocalDateTime.parse(newTripDTO.endsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .withHour(23).withMinute(59).withSecond(59).withNano(0);

        var newTrip = Trip.builder()
                .code(UUID.randomUUID())
                .owner(owner)
                .destination(newTripDTO.destination())
                .startsAt(startAt)
                .endsAt(endAt)
                .build();

        if (!newTripDTO.participantsEmail().isEmpty()) {
            var registeredUsers = userService.findAllByEmail(newTripDTO.participantsEmail());

            var newUsers = newTripDTO.participantsEmail().stream()
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
                    .filter(user -> newTripDTO.participantsEmail().contains(user.getEmail()))
                    .map(user -> TripParticipant.builder()
                            .user(user)
                            .trip(newTrip)
                            .build())
                    .toList();

            newTrip.setParticipants(participants);
        }

        var persistedTrip = tripService.save(newTrip);

        return new TripCodeDTO(persistedTrip.getCode());
    }

    @PutMapping("/{tripCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrip(@PathVariable("tripCode") UUID tripCode,
                           @RequestBody UpdatedTripDTO updatedTripDTO) {
        var trip = tripService.findByCode(tripCode).orElseThrow(TripNotFoundException::new);
        trip.setDestination(updatedTripDTO.destination());
        trip.setStartsAt(LocalDateTime.parse(updatedTripDTO.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
        trip.setEndsAt(LocalDateTime.parse(updatedTripDTO.endsAt(), DateTimeFormatter.ISO_DATE_TIME));

        tripService.save(trip);
    }

    @PatchMapping("/{tripCode}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmTrip(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCode(tripCode).orElseThrow(TripNotFoundException::new);
        trip.confirm();

        tripService.save(trip);
    }


    // Participants
    @GetMapping("/{tripCode}/participants")
    public List<UserDTO> findParticipantsByTripCode(
            @PathVariable("tripCode") UUID tripCode) {
        var participantsByTripCode = userService.findAllByTripCode(tripCode);

        return participantsByTripCode.stream()
                .map(userConverter::entityToDTO)
                .toList();
    }

    @PostMapping("/{tripCode}/participants")
    @ResponseStatus(HttpStatus.CREATED)
    public void inviteParticipants(@PathVariable UUID tripCode, @RequestBody NewParticipantDTO payload) {
        var trip = tripService.findByCode(tripCode).orElseThrow(TripNotFoundException::new);

        TripParticipant newParticipant;

        var userRegistered = userService.findByEmail(payload.email());

        if (userRegistered.isPresent()) {
            newParticipant = TripParticipant.builder()
                    .user(userRegistered.get())
                    .trip(trip)
                    .build();
        } else {
            var newUser = User.builder()
                    .code(UUID.randomUUID())
                    .email(payload.email())
                    .build();

            userService.save(newUser);

            newParticipant = TripParticipant.builder()
                    .user(newUser)
                    .trip(trip)
                    .build();
        }

        tripService.addParticipant(trip, newParticipant);
    }

    @DeleteMapping("/{tripCode}/participants/{participantCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeParticipant(@PathVariable("tripCode") UUID tripCode,
                                  @PathVariable("participantCode") UUID participantCode) {
        tripParticipantService.removeByTripCodeAndUserCode(tripCode, participantCode);
    }

    // Activities
    @GetMapping("/{tripCode}/activities")
    public List<DayActivitiesDTO> findActivitiesByTripCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCode(tripCode).orElseThrow(TripNotFoundException::new);

        var activitiesByTripCode = activityService.findActivitiesByTripCode(tripCode);
        var activitiesDTO = activitiesByTripCode.stream()
                .map(activityConverter::entityToDTO)
                .toList();

        var dates = getDatesBetween(trip.getStartsAt(), trip.getEndsAt());

        return dates.stream()
                .map((date) -> activityConverter.dtoAndDateToDayActivitiesDTO(activitiesDTO, date))
                .collect(Collectors.toList());
    }

    @PostMapping("/{tripCode}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public void createActivity(@PathVariable UUID tripCode, @RequestBody NewActivityDTO newActivityDTO) {
        var trip = tripService.findByCode(tripCode).orElseThrow(TripNotFoundException::new);

        var newActivity = Activity.builder()
                .trip(trip)
                .code(UUID.randomUUID())
                .title(newActivityDTO.title())
                .description(newActivityDTO.description())
                .occursAt(LocalDateTime.parse(newActivityDTO.occursAt(), DateTimeFormatter.ISO_DATE_TIME))
                .build();

        activityService.save(newActivity);
    }

    @DeleteMapping("/{tripCode}/activities/{activityCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeActivity(@PathVariable("tripCode") UUID tripCode,
                               @PathVariable("activityCode") UUID activityCode) {
        activityService.removeByCodeAndTripCode(activityCode, tripCode);
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
