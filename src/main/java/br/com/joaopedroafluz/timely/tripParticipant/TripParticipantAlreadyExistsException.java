package br.com.joaopedroafluz.timely.tripParticipant;

import br.com.joaopedroafluz.timely.exceptions.InvalidRequestException;

public class TripParticipantAlreadyExistsException extends InvalidRequestException {

    public TripParticipantAlreadyExistsException() {
        super("Participante com esse email já foi convidado para viagem");
    }

}
