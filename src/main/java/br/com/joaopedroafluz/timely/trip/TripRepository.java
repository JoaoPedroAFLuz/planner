package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByCode(UUID code);

    @Query("SELECT t FROM Trip t " +
            "LEFT JOIN TripParticipant tp ON t = tp.trip " +
            "WHERE t.owner = :user OR tp.user = :user " +
            "ORDER BY t.startsAt DESC")
    List<Trip> findAllByLoggedUser(User user);

}
