package br.com.joaopedroafluz.planner.activity;

import java.time.LocalDateTime;
import java.util.List;

public record ActivitiesResponseDTO(LocalDateTime date, List<ActivityDTO> activities) {
}
