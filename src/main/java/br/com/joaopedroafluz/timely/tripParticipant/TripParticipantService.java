package br.com.joaopedroafluz.timely.tripParticipant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripParticipantService {

    private final TripParticipantRepository tripParticipantRepository;


    public Optional<TripParticipant> findByTripCodeAndUserCode(UUID tripCode, UUID userCode) {
        return tripParticipantRepository.findByTripCodeAndUserCode(tripCode, userCode);
    }

    public void removeByTripCodeAndUserCode(UUID tripCode, UUID userCode) {
        findByTripCodeAndUserCode(tripCode, userCode).ifPresent(this::remove);
    }

    public void remove(TripParticipant tripParticipant) {
        tripParticipantRepository.delete(tripParticipant);
    }

}
