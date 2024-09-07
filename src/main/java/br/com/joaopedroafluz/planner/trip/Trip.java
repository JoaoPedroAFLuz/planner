package br.com.joaopedroafluz.planner.trip;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
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
    private ZonedDateTime startsAt;

    @Column(name = "ends_at", nullable = false)
    private ZonedDateTime endsAt;

    @Column(name = "confirmed_at")
    private ZonedDateTime confirmedAt;


    public Trip(TripRequestPayload payload) {
        this.code = UUID.randomUUID();
        this.ownerName = payload.ownerName();
        this.ownerEmail = payload.ownerEmail();
        this.destination = payload.destination();
        this.startsAt = ZonedDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
        this.endsAt = ZonedDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME);
    }
}
