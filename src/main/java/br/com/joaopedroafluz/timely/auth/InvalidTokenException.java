package br.com.joaopedroafluz.timely.auth;

import br.com.joaopedroafluz.timely.exceptions.InvalidRequestException;

public class InvalidTokenException extends InvalidRequestException {

    public InvalidTokenException(String message) {
        super(message);
    }

}
