package br.com.joaopedroafluz.timely.tripParticipant;

import br.com.joaopedroafluz.timely.exceptions.EntityNotFoundException;

public class TripParticipantNotFoundException extends EntityNotFoundException {

    public TripParticipantNotFoundException() {
        super("Participante não encontrado");
    }

}
