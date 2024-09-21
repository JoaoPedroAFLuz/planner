package br.com.joaopedroafluz.timely.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String refreshToken);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.user.email = :userEmail")
    void deleteAllByUserEmail(String userEmail);

}
