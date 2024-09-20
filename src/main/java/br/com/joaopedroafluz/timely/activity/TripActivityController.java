package br.com.joaopedroafluz.timely.activity;

import br.com.joaopedroafluz.timely.trip.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trips/{tripCode}/activities")
public class TripActivityController {

    private final TripService tripService;
    private final ActivityService activityService;
    private final ActivityConverter activityConverter;


    @GetMapping
    public List<DayActivitiesDTO> findActivitiesByTripCode(@PathVariable("tripCode") UUID tripCode) {
        var trip = tripService.findByCodeOrFail(tripCode);

        var activitiesByTripCode = activityService.findActivitiesByTripCode(tripCode);
        var activitiesDTO = activitiesByTripCode.stream()
                .map(activityConverter::entityToDTO)
                .toList();

        var dates = getDatesBetween(trip.getStartsAt(), trip.getEndsAt());

        return dates.stream()
                .map((date) -> activityConverter.dtoAndDateToDayActivitiesDTO(activitiesDTO, date))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createActivity(@PathVariable UUID tripCode, @RequestBody NewActivityDTO newActivityDTO) {
        var trip = tripService.findByCodeOrFail(tripCode);

        var newActivity = Activity.builder()
                .trip(trip)
                .code(UUID.randomUUID())
                .title(newActivityDTO.title())
                .description(newActivityDTO.description())
                .occursAt(LocalDateTime.parse(newActivityDTO.occursAt(), DateTimeFormatter.ISO_DATE_TIME))
                .build();

        activityService.save(newActivity);
    }

    @PutMapping("/{activityCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateActivity(@PathVariable UUID tripCode, @PathVariable UUID activityCode,
                               @RequestBody UpdatedActivityDTO updatedActivityDTO) {
        var activity = activityService.findByCodeAndTripCodeOrFail(activityCode, tripCode);

        activityConverter.copyToEntity(updatedActivityDTO, activity);

        activityService.save(activity);
    }

    @DeleteMapping("/{activityCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeActivity(@PathVariable("tripCode") UUID tripCode,
                               @PathVariable("activityCode") UUID activityCode) {
        activityService.removeByCodeAndTripCode(activityCode, tripCode);
    }

    public static List<LocalDateTime> getDatesBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }

}
