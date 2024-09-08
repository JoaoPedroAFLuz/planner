package br.com.joaopedroafluz.timely.participant;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByCode(UUID code);

    List<Participant> findAllByTripCode(UUID tripCode);

}
