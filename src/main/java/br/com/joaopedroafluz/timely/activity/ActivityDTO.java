package br.com.joaopedroafluz.timely.activity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    private UUID code;
    private UUID tripCode;
    private String title;
    private String description;
    private LocalDateTime occursAt;

}
