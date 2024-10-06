package br.com.joaopedroafluz.timely.tripParticipant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripParticipantDTO {

    private UUID code;
    private UUID tripCode;
    private String name;
    private String email;
    private LocalDateTime confirmedAt;

}
