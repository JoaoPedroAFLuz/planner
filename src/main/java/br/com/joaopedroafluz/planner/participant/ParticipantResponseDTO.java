package br.com.joaopedroafluz.planner.participant;


import java.time.LocalDateTime;
import java.util.UUID;

public record ParticipantResponseDTO(UUID code, UUID tripCode, String name, String email, LocalDateTime confirmedAt) {
}
