package br.com.joaopedroafluz.timely.authorization;

import br.com.joaopedroafluz.timely.user.User;
import br.com.joaopedroafluz.timely.user.UserNotFoundException;
import br.com.joaopedroafluz.timely.user.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private final UserService userService;


    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public Optional<UserDetailsAdapter> getUserDetailsFromTokenIfValid(String authorizationHeader) {
        var token = recoverToken(authorizationHeader);

        if (token == null) {
            return Optional.empty();
        }

        final var isInvalidToken = !isValidToken(token);

        if (isInvalidToken) {
            return Optional.empty();
        }

        final var username = getUsername(token);
        final var usuario = userService.findByEmail(username).orElseThrow(UserNotFoundException::new);
        final var userDetails = new UserDetailsAdapter(usuario);

        return Optional.of(userDetails);
    }

    private Map<String, Claim> getClaims(String token) {
        var algorithm = Algorithm.HMAC256(secret);

        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getClaims();

    }

    private boolean isValidToken(String token) {
        final var claims = getClaims(token);
        final var username = claims.get("sub").as(String.class);
        final var expirationDate = claims.get("exp").asDate();
        final var now = new Date(System.currentTimeMillis());

        return Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate);
    }

    private String getUsername(String token) {
        final var claims = getClaims(token);
        return claims.get("sub").as(String.class);
    }


    private String recoverToken(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        return authorizationHeader.replace("Bearer ", "");
    }

    private Instant genExpirationDate() {
        return Instant.now().plus(10, ChronoUnit.MINUTES);
    }

}


