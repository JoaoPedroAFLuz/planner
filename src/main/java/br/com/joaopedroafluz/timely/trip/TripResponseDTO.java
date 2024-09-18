package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.user.UserDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripResponseDTO(UUID code, UserDTO owner, String destination, LocalDateTime startsAt,
                              LocalDateTime endsAt, LocalDateTime createdAt, LocalDateTime confirmedAt) {
}
