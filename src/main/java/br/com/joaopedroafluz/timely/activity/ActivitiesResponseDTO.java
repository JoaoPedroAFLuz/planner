package br.com.joaopedroafluz.timely.activity;

import java.time.LocalDateTime;
import java.util.List;

public record ActivitiesResponseDTO(LocalDateTime date, List<ActivityDTO> activities) {
}
