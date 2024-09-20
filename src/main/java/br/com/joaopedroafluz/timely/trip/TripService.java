package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.tripParticipant.TripParticipant;
import br.com.joaopedroafluz.timely.user.User;
import br.com.joaopedroafluz.timely.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static br.com.joaopedroafluz.timely.auth.AuthorizationUtils.throwExceptionIfUserDoesNotHavePermission;

@Service
@AllArgsConstructor
public class TripService {

    private final UserService userService;
    private final TripConverter tripConverter;
    private final TripRepository tripRepository;


    public Trip findByCodeOrFail(UUID code) {
        var trip = tripRepository.findByCode(code)
                .orElseThrow(TripNotFoundException::new);

        throwExceptionIfUserDoesNotHavePermission(trip);

        return trip;
    }

    public Trip register(NewTripDTO newTripDTO) {
        var newTrip = tripConverter.dtoToEntity(newTripDTO);

        if (!newTripDTO.getParticipantsEmail().isEmpty()) {
            var registeredUsers = userService.findAllByEmail(newTripDTO.getParticipantsEmail());

            var newUsers = newTripDTO.getParticipantsEmail().stream()
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
                    .filter(user -> newTripDTO.getParticipantsEmail().contains(user.getEmail()))
                    .map(user -> TripParticipant.builder()
                            .user(user)
                            .trip(newTrip)
                            .build())
                    .toList();

            newTrip.setParticipants(participants);
        }

        return save(newTrip);
    }

    public void addParticipant(Trip trip, TripParticipant participant) {
        trip.getParticipants().add(participant);

        throwExceptionIfUserDoesNotHavePermission(trip);

        tripRepository.save(trip);
    }

    public void confirm(Trip trip) {
        throwExceptionIfUserDoesNotHavePermission(trip.getOwner().getCode());

        trip.confirm();
        save(trip);
    }

    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

}
