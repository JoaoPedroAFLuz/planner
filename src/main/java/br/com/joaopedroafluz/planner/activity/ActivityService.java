package br.com.joaopedroafluz.planner.activity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Optional<Activity> findByCode(UUID code) {
        return activityRepository.findByCode(code);
    }

    public List<Activity> findActivitiesByTripCode(UUID tripCode) {
        return activityRepository.findAllByTripCode(tripCode);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void remove(Activity activity) {
        activityRepository.delete(activity);
    }

}
