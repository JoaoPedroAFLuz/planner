package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.tripParticipant.TripParticipant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripService {

    private final TripRepository tripRepository;


    public Optional<Trip> findByCode(UUID code) {
        return tripRepository.findByCode(code);
    }

    public void addParticipant(Trip trip, TripParticipant participant) {
        trip.getParticipants().add(participant);

        tripRepository.save(trip);
    }

    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

}
