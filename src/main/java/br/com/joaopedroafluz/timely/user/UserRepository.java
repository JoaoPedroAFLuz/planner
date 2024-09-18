package br.com.joaopedroafluz.timely.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCode(UUID code);

    Optional<User> findByEmail(String email);

    List<User> findAllByTripsAsParticipantTripCode(UUID tripCode);

    @Query("SELECT u FROM User u WHERE u.email IN :emails")
    List<User> findAllByEmails(List<String> emails);

}
