package br.com.joaopedroafluz.planner.participant;

import br.com.joaopedroafluz.planner.trip.Trip;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public Optional<Participant> findByCode(UUID code) {
        return participantRepository.findByCode(code);
    }

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

    public void registerParticipantsTotEvent(Participant owner,
                                             List<String> participantsEmailsToInvite,
                                             Trip trip) {
        Set<Participant> participants = new LinkedHashSet<>();
        participants.add(owner);

        participantsEmailsToInvite.forEach(email -> {
            var participant = Participant.builder()
                    .email(email)
                    .code(UUID.randomUUID())
                    .trip(trip).build();

            participants.add(participant);
        });

        participantRepository.saveAll(participants);
    }

    public void confirmParticipant(Participant participant, String participantName) {
        participant.setName(participantName);
        participant.setConfirmedAt(LocalDateTime.now());

        participantRepository.save(participant);
    }

    public void triggerConfirmationEmailToParticipant(String email) {
    }

    public void triggerConfirmationEmailToParticipants(UUID tripCode) {
    }

    @Transactional
    public void removeParticipant(Participant participant) {
        participantRepository.delete(participant);
    }

}
