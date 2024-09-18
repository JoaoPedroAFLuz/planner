package br.com.joaopedroafluz.timely.activity;

import br.com.joaopedroafluz.timely.exceptions.InvalidRequestException;

public class InvalidActivityRequestException extends InvalidRequestException {

    public InvalidActivityRequestException(String message) {
        super(message);
    }

}
