package br.com.joaopedroafluz.timely.tripParticipant;

import br.com.joaopedroafluz.timely.trip.NewParticipantDTO;
import br.com.joaopedroafluz.timely.trip.TripService;
import br.com.joaopedroafluz.timely.user.User;
import br.com.joaopedroafluz.timely.user.UserConverter;
import br.com.joaopedroafluz.timely.user.UserDTO;
import br.com.joaopedroafluz.timely.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/{tripCode}/participants")
public class TripParticipantController {

    private final TripService tripService;
    private final UserService userService;
    private final UserConverter userConverter;
    private final TripParticipantService tripParticipantService;


    @GetMapping
    public List<UserDTO> findParticipantsByTripCode(@PathVariable("tripCode") UUID tripCode) {
        var participantsByTripCode = userService.findAllByTripCode(tripCode);

        return participantsByTripCode.stream()
                .map(userConverter::entityToDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void inviteParticipants(@PathVariable UUID tripCode, @RequestBody NewParticipantDTO newParticipantDTO) {
        var trip = tripService.findByCodeOrFail(tripCode);

        var participantAlreadyInvited = trip.getParticipants().stream()
                .anyMatch(participant -> participant.getUser().getEmail().equals(newParticipantDTO.email()));

        if (participantAlreadyInvited) {
            throw new TripParticipantAlreadyExistsException();
        }

        TripParticipant newParticipant;

        var userRegistered = userService.findByEmail(newParticipantDTO.email());

        if (userRegistered.isPresent()) {
            newParticipant = TripParticipant.builder()
                    .user(userRegistered.get())
                    .trip(trip)
                    .build();
        } else {
            var newUser = User.builder()
                    .code(UUID.randomUUID())
                    .email(newParticipantDTO.email())
                    .build();

            userService.save(newUser);

            newParticipant = TripParticipant.builder()
                    .user(newUser)
                    .trip(trip)
                    .build();
        }

        tripService.addParticipant(trip, newParticipant);
    }

    @DeleteMapping("/{participantCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeParticipant(@PathVariable("tripCode") UUID tripCode,
                                  @PathVariable("participantCode") UUID participantCode) {
        tripParticipantService.removeByTripCodeAndUserCode(tripCode, participantCode);
    }

}
