package br.com.joaopedroafluz.timely.trip;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final TripConverter tripConverter;

    @GetMapping()
    public List<ResumedTripDTO> findUserTrips() {
        var trip = tripService.findAllByLoggedUser();

        return trip.stream()
                .map(tripConverter::entityToResumedDTO)
                .toList();
    }

    @GetMapping("/{tripCode}")
    public TripDTO findTripByCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCodeOrFail(tripCode);

        return tripConverter.entityToDTO(trip);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripCodeDTO createTrip(@RequestBody NewTripDTO newTripDTO) {
        var newTrip = tripService.register(newTripDTO);

        return new TripCodeDTO(newTrip.getCode());
    }

    @PutMapping("/{tripCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrip(@PathVariable("tripCode") UUID tripCode,
                           @RequestBody UpdatedTripDTO updatedTripDTO) {
        var trip = tripService.findByCodeOrFail(tripCode);

        var startAt = LocalDateTime.parse(updatedTripDTO.startsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        var endAt = LocalDateTime.parse(updatedTripDTO.endsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .withHour(23).withMinute(59).withSecond(59).withNano(0);

        trip.setDestination(updatedTripDTO.destination());
        trip.setStartsAt(startAt);
        trip.setEndsAt(endAt);

        tripService.save(trip);
    }

    @PatchMapping("/{tripCode}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmTrip(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCodeOrFail(tripCode);

        tripService.confirm(trip);
    }

}
