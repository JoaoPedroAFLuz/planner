package br.com.joaopedroafluz.timely.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final ModelMapper modelMapper;


    public UserDTO entityToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User dtoToEntity(NewUserDTO newUserDTO) {
        return modelMapper.map(newUserDTO, User.class);
    }

}
