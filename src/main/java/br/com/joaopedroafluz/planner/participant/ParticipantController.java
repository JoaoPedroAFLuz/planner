package br.com.joaopedroafluz.planner.participant;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantRepository participantRepository;

    @PatchMapping("/{participantCode}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID participantCode,
                                                          @RequestBody ParticipantRequestPayload payload) {
        var participant = participantRepository.findByCode(participantCode);

        if (participant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        participant.get().setConfirmedAt(LocalDateTime.now());
        participant.get().setName(payload.name());

        participantRepository.save(participant.get());

        return ResponseEntity.ok(participant.get());
    }

}
