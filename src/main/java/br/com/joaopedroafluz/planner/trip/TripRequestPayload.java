package br.com.joaopedroafluz.planner.trip;

import java.util.List;

public record TripRequestPayload(
        String ownerName,
        String ownerEmail,
        List<String> emailsToInvite,
        String destination,
        String startsAt,
        String endsAt
) {
}
