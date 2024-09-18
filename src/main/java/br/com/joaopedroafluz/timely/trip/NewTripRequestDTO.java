package br.com.joaopedroafluz.timely.trip;

import java.util.List;
import java.util.UUID;

public record NewTripRequestDTO(
        UUID ownerCode,
        List<String> participantsEmail,
        String destination,
        String startsAt,
        String endsAt
) {
}
