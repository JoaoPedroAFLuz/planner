package br.com.joaopedroafluz.timely.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID code;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

    private String destination;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;


    public Trip(TripRequestPayload payload) {
        this.code = UUID.randomUUID();
        this.ownerName = payload.ownerName();
        this.ownerEmail = payload.ownerEmail();
        this.destination = payload.destination();
        this.startsAt = LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .toLocalDate()
                .atStartOfDay();
        this.endsAt = LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME)
                .toLocalDate()
                .atStartOfDay()
                .plusDays(1)
                .minusSeconds(1);
    }
}
