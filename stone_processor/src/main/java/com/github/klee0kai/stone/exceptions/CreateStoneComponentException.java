package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class CreateStoneComponentException extends StoneException {

    public CreateStoneComponentException(Element ownerElement, Throwable cause) {
        super("Cannot create component: " + ownerElement + "\ncaused " + cause.getMessage(), cause);
    }

}
