package br.com.joaopedroafluz.timely.activity;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActivityConverter {

    private final ModelMapper modelMapper;


    public ActivityDTO entityToDTO(Activity activity) {
        return modelMapper.map(activity, ActivityDTO.class);
    }

    public void copyToEntity(UpdatedActivityDTO activityDTO, Activity activity) {
        modelMapper.map(activityDTO, activity);
    }

    public DayActivitiesDTO dtoAndDateToDayActivitiesDTO(List<ActivityDTO> activityResponseDTOs, LocalDateTime date) {
        return new DayActivitiesDTO(date,
                activityResponseDTOs.stream()
                        .filter((activity) -> activity.getOccursAt().getDayOfYear() == date.getDayOfYear())
                        .sorted(Comparator.comparing(ActivityDTO::getOccursAt))
                        .collect(Collectors.toList()));
    }

}
