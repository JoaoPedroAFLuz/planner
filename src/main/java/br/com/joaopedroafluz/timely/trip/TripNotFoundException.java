package br.com.joaopedroafluz.timely.trip;

import br.com.joaopedroafluz.timely.exceptions.EntityNotFoundException;

public class TripNotFoundException extends EntityNotFoundException {

    public TripNotFoundException() {
        super("Viagem não encontrada");
    }

}

