package br.com.joaopedroafluz.timely.tripParticipant;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TripParticipantService {

    private final TripParticipantRepository tripParticipantRepository;


    public TripParticipant findByTripCodeAndUserCodeOrFail(UUID tripCode, UUID userCode) {
        return tripParticipantRepository.findByTripCodeAndUserCode(tripCode, userCode)
                .orElseThrow(TripParticipantNotFoundException::new);
    }

    public List<TripParticipant> findAllByTripCode(UUID tripCode) {
        return tripParticipantRepository.findAllByTripCode(tripCode);
    }

    public void removeByTripCodeAndUserCode(UUID tripCode, UUID userCode) {
        var tripParticipant = findByTripCodeAndUserCodeOrFail(tripCode, userCode);

        remove(tripParticipant);
    }

    public void remove(TripParticipant tripParticipant) {
        tripParticipantRepository.delete(tripParticipant);
    }

}
