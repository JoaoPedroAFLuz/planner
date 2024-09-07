package br.com.joaopedroafluz.planner.link;

import java.util.UUID;

public record LinkResponseDTO(UUID activityCode, UUID linkCode, String title, String url) {
}
