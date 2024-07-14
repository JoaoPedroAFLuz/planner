package br.com.joaopedroafluz.planner.participant;

import br.com.joaopedroafluz.planner.trip.Trip;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    private UUID code;

    private String name;

    private String email;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

}
