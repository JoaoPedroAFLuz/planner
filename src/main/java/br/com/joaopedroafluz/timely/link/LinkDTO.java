package br.com.joaopedroafluz.timely.link;

import java.util.UUID;

public record LinkDTO(UUID activityCode, UUID linkCode, String title, String url) {
}
