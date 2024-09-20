package br.com.joaopedroafluz.timely.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewTripDTO {

    private UUID ownerCode;
    private List<String> participantsEmail;
    private String destination;
    private String startsAt;
    private String endsAt;

}
