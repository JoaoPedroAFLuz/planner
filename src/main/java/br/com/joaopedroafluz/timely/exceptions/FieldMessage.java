package br.com.joaopedroafluz.timely.exceptions;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FieldMessage {

	private String fieldName;
	private String message;
	
}
