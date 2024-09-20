package br.com.joaopedroafluz.timely.link;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkDTO {

    private UUID code;
    private UUID activityCode;
    private String title;
    private String url;

}
