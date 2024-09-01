package br.com.joaopedroafluz.planner.participant;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/participants")
public class ParticipantController {

    public final ParticipantService participantService;

    @PatchMapping("/{participantCode}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID participantCode,
                                                          @RequestBody ParticipantRequestPayload payload) {
        var participant = participantService.findByCode(participantCode);

        if (participant.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        participantService.confirmParticipant(participant.get(), payload.name());

        return ResponseEntity.ok(participant.get());
    }

}
