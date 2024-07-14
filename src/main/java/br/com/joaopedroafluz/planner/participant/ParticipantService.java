package br.com.joaopedroafluz.planner.participant;

import br.com.joaopedroafluz.planner.trip.Trip;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public List<Participant> findParticipantsByTripCode(UUID tripCode) {
        return participantRepository.findAllByTripCode(tripCode);
    }

    public Participant registerParticipantTotEvent(String participantEmailToInvite, Trip trip) {
        var participant = Participant.builder()
                .trip(trip)
                .code(UUID.randomUUID())
                .email(participantEmailToInvite)
                .build();

        return participantRepository.save(participant);
    }

    public void registerParticipantsTotEvent(List<String> participantsEmailsToInvite, Trip trip) {
        var participants = participantsEmailsToInvite.stream()
                .map(email -> Participant.builder()
                        .email(email)
                        .code(UUID.randomUUID())
                        .trip(trip).build())
                .collect(Collectors.toSet());

        participantRepository.saveAll(participants);
    }

    public void triggerConfirmationEmailToParticipant(String email) {
    }

    public void triggerConfirmationEmailToParticipants(UUID tripCode) {
    }

}
