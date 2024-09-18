package br.com.joaopedroafluz.timely.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUser(@RequestBody NewUserDTO newUserDTO) {
        var user = userConverter.dtoToEntity(newUserDTO);
        var registeredUser = userService.registerUser(user);

        return userConverter.entityToDTO(registeredUser);
    }

}
