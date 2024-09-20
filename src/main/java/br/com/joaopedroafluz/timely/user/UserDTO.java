package br.com.joaopedroafluz.timely.user;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID code;
    private String name;
    private String email;

}
