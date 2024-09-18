package br.com.joaopedroafluz.timely.activity;

import br.com.joaopedroafluz.timely.exceptions.EntityNotFoundException;

public class ActivityNotFoundException extends EntityNotFoundException {

    public ActivityNotFoundException() {
        super("Atividade não encontrada");
    }

}
