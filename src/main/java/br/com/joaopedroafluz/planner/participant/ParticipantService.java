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

    public void registerParticipantsTotEvent(List<String> participantsEmailsToInvite, Trip trip) {
        var participants = participantsEmailsToInvite.stream()
                .map(email -> Participant.builder()
                        .email(email)
                        .code(UUID.randomUUID())
                        .trip(trip).build())
                .collect(Collectors.toSet());

        participantRepository.saveAll(participants);
    }

    public void triggerConfirmationEmailToParticipant(UUID tripCode) {
    }

}
