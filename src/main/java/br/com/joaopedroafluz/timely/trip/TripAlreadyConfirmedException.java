package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.exceptions.InvalidRequestException;

public class TripAlreadyConfirmedException extends InvalidRequestException {

    public TripAlreadyConfirmedException() {
        super("Viagem já confirmada");
    }

}
