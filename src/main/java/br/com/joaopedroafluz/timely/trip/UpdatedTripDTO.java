package br.com.joaopedroafluz.timely.trip;

public record UpdatedTripDTO(
        String destination,
        String startsAt,
        String endsAt
) {
}
