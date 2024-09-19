package br.com.joaopedroafluz.timely.activity;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityConverter {

    public ActivityDTO entityToDTO(Activity activity) {
        return new ActivityDTO(
                activity.getCode(),
                activity.getTrip().getCode(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getOccursAt());
    }

    public DayActivitiesDTO dtoAndDateToDayActivitiesDTO(List<ActivityDTO> activityResponseDTOS, LocalDateTime date) {
        return new DayActivitiesDTO(date,
                activityResponseDTOS.stream()
                        .filter((activity) -> activity.occursAt().getDayOfYear() == date.getDayOfYear())
                        .sorted(Comparator.comparing(ActivityDTO::occursAt))
                        .collect(Collectors.toList()));
    }

}
