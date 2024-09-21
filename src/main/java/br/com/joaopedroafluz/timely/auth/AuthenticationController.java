package br.com.joaopedroafluz.timely.auth;

import br.com.joaopedroafluz.timely.user.NewUserDTO;
import br.com.joaopedroafluz.timely.user.UserConverter;
import br.com.joaopedroafluz.timely.user.UserDTO;
import br.com.joaopedroafluz.timely.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public LoginDTO login(@RequestBody NewLoginDTO newLoginDTO) {
        var user = userService.findByEmail(newLoginDTO.username())
                .orElseThrow(() -> new InvalidLoginRequestException("Credenciais inválidas"));

        var isPasswordCorrect = passwordEncoder.matches(newLoginDTO.password(), user.getPassword());

        if (!isPasswordCorrect) {
            throw new InvalidLoginRequestException("Credenciais inválidas");
        }

        var accessToken = accessTokenService.generateAccessToken(user);
        var refreshToken = refreshTokenService.generateToken(user);

        return new LoginDTO(accessToken, refreshToken.getToken());
    }

    @PostMapping("/refresh")
    public LoginDTO refresh(@RequestBody NewRefreshTokenDTO newRefreshTokenDTO) {
        var refreshToken = refreshTokenService.regenerateRefreshToken(newRefreshTokenDTO.refreshToken());
        var accessToken = accessTokenService.generateAccessToken(refreshToken.getUser());

        return new LoginDTO(accessToken, refreshToken.getToken());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO register(@RequestBody NewUserDTO newUserDTO) {
        var user = userConverter.dtoToEntity(newUserDTO);
        var registeredUser = userService.registerUser(user);

        return userConverter.entityToDTO(registeredUser);
    }

}
