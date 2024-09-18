package br.com.joaopedroafluz.timely.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> registerUser(@RequestBody NewUserDTO newUserDTO) {
        userService.findByEmail(newUserDTO.email()).ifPresent(user -> {
            throw new IllegalStateException("Email already in use");
        });

        var user = User.builder()
                .code(UUID.randomUUID())
                .name(newUserDTO.name())
                .email(newUserDTO.email())
                .password(newUserDTO.password())
                .build();

        var registeredUser = userService.registerUser(user);

        var userDTO = new UserDTO(registeredUser.getCode(), registeredUser.getName(), registeredUser.getEmail());

        return ResponseEntity.ok(userDTO);
    }

}
