package br.com.joaopedroafluz.planner.activity;

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
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    private UUID code;

    private String title;

    @Column(name = "occurs_at")
    private LocalDateTime occursAt;

}
