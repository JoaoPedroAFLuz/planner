package br.com.joaopedroafluz.planner.participant;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    public void registerParticipantsTotEvent(List<String> participantsEmailsToInvite, UUID tripCode) {
    }

    public void triggerConfirmationEmailToParticipant(UUID tripCode) {
    }

}
