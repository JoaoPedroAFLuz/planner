package br.com.joaopedroafluz.timely.user;

import br.com.joaopedroafluz.timely.exceptions.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super("Usuário não encontrado");
    }

}
