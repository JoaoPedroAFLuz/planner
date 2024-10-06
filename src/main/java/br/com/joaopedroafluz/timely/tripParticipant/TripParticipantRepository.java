package br.com.joaopedroafluz.timely.tripParticipant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TripParticipantRepository extends JpaRepository<TripParticipant, Long> {

    Optional<TripParticipant> findByTripCodeAndUserCode(UUID tripCode, UUID userCode);

    List<TripParticipant> findAllByTripCode(UUID tripCode);

}
