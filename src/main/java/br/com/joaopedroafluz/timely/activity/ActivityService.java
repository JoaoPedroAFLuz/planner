package br.com.joaopedroafluz.timely.activity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.joaopedroafluz.timely.auth.AuthorizationUtils.throwExceptionIfUserDoesNotHavePermission;

@Service
@AllArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;


    public Activity findByCodeOrFail(UUID code) {
        var activity = activityRepository.findByCode(code).orElseThrow(ActivityNotFoundException::new);

        throwExceptionIfUserDoesNotHavePermission(activity.getTrip());

        return activity;
    }

    public Activity findByCodeAndTripCodeOrFail(UUID code, UUID tripCode) {
        var activity = activityRepository.findByCodeAndTripCode(code, tripCode)
                .orElseThrow(ActivityNotFoundException::new);

        throwExceptionIfUserDoesNotHavePermission(activity.getTrip());

        return activity;
    }

    public List<Activity> findActivitiesByTripCode(UUID tripCode) {
        return activityRepository.findAllByTripCode(tripCode);
    }

    public void save(Activity activity) {
        var trip = activity.getTrip();

        throwExceptionIfUserDoesNotHavePermission(activity.getTrip());

        if (activity.getOccursAt().isBefore(trip.getStartsAt()) || activity.getOccursAt().isAfter(trip.getEndsAt())) {
            throw new InvalidActivityRequestException("O horário da atividade deve estar dentro do período da viagem.");
        }

        activityRepository.save(activity);
    }

    public void removeByCodeAndTripCode(UUID activityCode, UUID tripCode) {
        var activity = findByCodeAndTripCodeOrFail(activityCode, tripCode);

        throwExceptionIfUserDoesNotHavePermission(activity.getTrip());

        remove(activity);
    }

    public void remove(Activity activity) {
        activityRepository.delete(activity);
    }

}
