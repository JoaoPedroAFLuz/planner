package br.com.joaopedroafluz.timely.auth;

import br.com.joaopedroafluz.timely.exceptions.InvalidRequestException;
import br.com.joaopedroafluz.timely.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.Claim;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${api.security.token.refresh_secret}")
    private String refreshTokenSecret;

    private final RefreshTokenRepository refreshTokenRepository;


    public Optional<RefreshToken> findByTokenOrFail(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken);
    }

    public boolean isValidRefreshToken(String refreshToken) {
        final var claims = getClaims(refreshToken);
        final var username = claims.get("sub").as(String.class);
        final var expirationDate = claims.get("exp").asDate();
        final var now = new Date(System.currentTimeMillis());

        return Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate);
    }

    @Transactional
    public RefreshToken generateToken(User user) {
        var token = createToken(user);

        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .build();

        save(refreshToken);

        return refreshToken;
    }

    @Transactional(noRollbackFor = InvalidRequestException.class)
    public RefreshToken regenerateRefreshToken(String refreshToken) {
        if (!isValidRefreshToken(refreshToken)) {
            removeByToken(refreshToken);
            throw new InvalidRequestException("Refresh token inválido");
        }
        var savedToken = findByTokenOrFail(refreshToken);

        if (savedToken.isEmpty()) {
            removeAllByUserEmail(getUsername(refreshToken));
            throw new InvalidRequestException("Refresh token inválido");
        }

        var user = savedToken.get().getUser();
        var newToken = createToken(user);

        var newRefreshToken = RefreshToken.builder()
                .user(user)
                .token(newToken)
                .build();

        save(newRefreshToken);
        removeByToken(refreshToken);

        return newRefreshToken;
    }

    @Transactional
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public void removeByToken(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Transactional
    public void removeAllByUserEmail(String userEmail) {
        refreshTokenRepository.deleteAllByUserEmail(userEmail);
    }

    public String createToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(refreshTokenSecret);

            return JWT.create()
                    .withIssuer("timely-api")
                    .withSubject(user.getEmail())
                    .withIssuedAt(new Date())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    private Map<String, Claim> getClaims(String refreshToken) {
        var algorithm = Algorithm.HMAC256(refreshTokenSecret);

        return JWT.require(algorithm)
                .withIssuer("timely-api")
                .build()
                .verify(refreshToken)
                .getClaims();
    }

    private String getUsername(String token) {
        final var claims = getClaims(token);
        return claims.get("sub").as(String.class);
    }

    private Instant genExpirationDate() {
        return Instant.now().plus(10, ChronoUnit.DAYS);
    }

}
