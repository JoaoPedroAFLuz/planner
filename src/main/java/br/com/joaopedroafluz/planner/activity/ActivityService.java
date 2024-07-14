package br.com.joaopedroafluz.planner.activity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> findActivitiesByTripCode(UUID tripCode) {
        return activityRepository.findAllByTripCode(tripCode);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

}
