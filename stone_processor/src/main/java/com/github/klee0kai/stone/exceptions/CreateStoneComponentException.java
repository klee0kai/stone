package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.cannotCreateComponent;

public class CreateStoneComponentException extends StoneException {

    public CreateStoneComponentException(Element ownerElement, Throwable cause) {
        super(StoneExceptionStrings.collectCauseMessages(String.format(cannotCreateComponent, ownerElement), cause), cause);
    }

}
