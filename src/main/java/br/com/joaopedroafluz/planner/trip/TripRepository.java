package br.com.joaopedroafluz.planner.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findByCode(UUID code);

}
