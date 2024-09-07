package br.com.joaopedroafluz.planner.activity;

import java.time.ZonedDateTime;
import java.util.List;

public record ActivitiesResponseDTO(ZonedDateTime date, List<ActivityDTO> activities) {
}
