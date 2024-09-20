package br.com.joaopedroafluz.timely.link;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByCodeAndActivityCode(UUID linkCode, UUID activityCode);

    List<Link> findAllByActivityCode(UUID tripCode);

}
