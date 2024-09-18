package br.com.joaopedroafluz.timely.exceptions;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private Integer status;
    private String error;
    private String message;
    private String path;

    public long getTimestamp() {
        return System.currentTimeMillis();
    }

}
