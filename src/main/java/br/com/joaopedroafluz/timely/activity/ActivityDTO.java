package br.com.joaopedroafluz.timely.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO(UUID code, UUID tripCode, String title, String description, LocalDateTime occursAt) {
}
