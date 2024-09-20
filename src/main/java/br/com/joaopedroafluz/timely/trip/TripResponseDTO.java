package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripResponseDTO {

    private UUID code;
    private UserDTO owner;
    private String destination;
    private LocalDateTime startsAt;
    private java.time.LocalDateTime endsAt;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;

}
