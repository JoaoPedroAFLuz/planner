package br.com.joaopedroafluz.timely.participant;


import java.time.LocalDateTime;
import java.util.UUID;

public record ParticipantResponseDTO(UUID tripCode, UUID code, String name, String email, LocalDateTime confirmedAt) {
}
