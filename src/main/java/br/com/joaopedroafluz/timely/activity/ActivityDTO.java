package br.com.joaopedroafluz.timely.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO(UUID tripCode, UUID code, String title, String description, LocalDateTime occursAt) {
}
