package br.com.joaopedroafluz.planner.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, Long> {


    List<Activity> findAllByTripCode(UUID tripCode);

}
