package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.auth.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TripConverter {

    private final ModelMapper modelMapper;

    public TripDTO entityToDTO(Trip trip) {
        return modelMapper.map(trip, TripDTO.class);
    }

    public ResumedTripDTO entityToResumedDTO(Trip trip) {
        return modelMapper.map(trip, ResumedTripDTO.class);
    }

    public Trip dtoToEntity(NewTripDTO newTripDTO) {
        var owner = AuthorizationUtils.getAuthenticatedUser();

        var startAt = LocalDateTime.parse(newTripDTO.getStartsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        var endAt = LocalDateTime.parse(newTripDTO.getEndsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .withHour(23).withMinute(59).withSecond(59).withNano(0);

        return Trip.builder()
                .code(UUID.randomUUID())
                .owner(owner)
                .destination(newTripDTO.getDestination())
                .startsAt(startAt)
                .endsAt(endAt)
                .build();
    }

}


