package br.com.joaopedroafluz.timely.user;

import java.util.UUID;

public record UserDTO(UUID code, String name, String email) {
}
