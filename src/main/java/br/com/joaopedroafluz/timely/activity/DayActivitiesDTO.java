package br.com.joaopedroafluz.timely.activity;

import java.time.LocalDateTime;
import java.util.List;

public record DayActivitiesDTO(LocalDateTime date, List<ActivityDTO> activities) {
}
