package br.com.joaopedroafluz.planner.activity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

}
