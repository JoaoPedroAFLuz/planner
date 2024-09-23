package br.com.joaopedroafluz.timely.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedActivityDTO {

    private String title;
    private String description;
    private LocalDateTime occursAt;

}
