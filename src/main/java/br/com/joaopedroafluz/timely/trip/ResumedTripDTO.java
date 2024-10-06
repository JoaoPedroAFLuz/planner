package br.com.joaopedroafluz.timely.trip;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumedTripDTO {

    private UUID code;
    private UUID ownerCode;
    private String destination;
    private java.time.LocalDateTime startsAt;
    private LocalDateTime endsAt;


}
