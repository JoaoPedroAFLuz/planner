package br.com.joaopedroafluz.timely.activity;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;
    private final ActivityConverter activityConverter;


    @GetMapping("/{activityCode}")
    public ActivityDTO findByCode(@PathVariable("activityCode") UUID activityCode) {
        var activity = activityService.findByCodeOrFail(activityCode);

        return activityConverter.entityToDTO(activity);
    }

}
