package br.com.joaopedroafluz.planner.link;

import java.util.UUID;

public record LinkResponseDTO(UUID tripCode, UUID linkCode, String title, String url) {
}
