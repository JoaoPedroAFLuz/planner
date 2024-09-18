package br.com.joaopedroafluz.timely.tripParticipant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripParticipantService {

    private final TripParticipantRepository tripParticipantRepository;


    public Optional<TripParticipant> findByCode(UUID tripCode, UUID userCode) {
        return tripParticipantRepository.findByTripCodeAndUserCode(tripCode, userCode);
    }

    public void remove(TripParticipant tripParticipant) {
        tripParticipantRepository.delete(tripParticipant);
    }

}
