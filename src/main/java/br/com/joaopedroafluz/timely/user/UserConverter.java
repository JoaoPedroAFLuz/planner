package br.com.joaopedroafluz.timely.user;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserConverter {

    public UserDTO entityToDTO(User user) {
        return new UserDTO(user.getCode(), user.getName(), user.getEmail());
    }

    public User dtoToEntity(NewUserDTO newUserDTO) {
        return User.builder()
                .code(UUID.randomUUID())
                .name(newUserDTO.name())
                .email(newUserDTO.email())
                .password(newUserDTO.password())
                .build();
    }

}
