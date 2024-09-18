package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class TripConverter {

    public TripResponseDTO entityToDTO(Trip trip, UserDTO ownerDTO) {
        return new TripResponseDTO(trip.getCode(), ownerDTO, trip.getDestination(),
                trip.getStartsAt(), trip.getEndsAt(), trip.getCreatedAt(),
                trip.getConfirmedAt());
    }

}


