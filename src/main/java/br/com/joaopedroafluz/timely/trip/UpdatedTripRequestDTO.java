package br.com.joaopedroafluz.timely.trip;

public record UpdatedTripRequestDTO(
        String destination,
        String startsAt,
        String endsAt
) {
}
