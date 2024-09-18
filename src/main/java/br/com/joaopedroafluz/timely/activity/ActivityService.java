package br.com.joaopedroafluz.timely.activity;

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

    public void save(Activity activity) {
        var trip = activity.getTrip();

        if (activity.getOccursAt().isBefore(trip.getStartsAt()) || activity.getOccursAt().isAfter(trip.getEndsAt())) {
            throw new InvalidActivityRequestException("O horário da atividade deve estar dentro do período da viagem.");
        }

        activityRepository.save(activity);
    }

    public void removeByCodeAndTripCode(UUID activityCode, UUID tripCode) {
        var activity = findByCode(activityCode).orElseThrow(ActivityNotFoundException::new);

        if (activity.getTrip().getCode().equals(tripCode)) {
            remove(activity);
        } else {
            throw new InvalidActivityRequestException("Atividade não pertence à viagem informada");
        }
    }

    public void remove(Activity activity) {
        activityRepository.delete(activity);
    }

}
