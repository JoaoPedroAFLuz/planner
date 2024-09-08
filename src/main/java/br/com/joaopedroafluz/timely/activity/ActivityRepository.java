package br.com.joaopedroafluz.timely.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Optional<Activity> findByCode(UUID code);

    List<Activity> findAllByTripCode(UUID tripCode);

}
