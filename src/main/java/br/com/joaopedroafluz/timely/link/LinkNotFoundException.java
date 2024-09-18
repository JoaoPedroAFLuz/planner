package br.com.joaopedroafluz.timely.link;

import br.com.joaopedroafluz.timely.exceptions.EntityNotFoundException;

public class LinkNotFoundException extends EntityNotFoundException {

    public LinkNotFoundException() {
        super("Link não encontrado");
    }

}
