package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.cannotCreateModule;

public class CreateStoneModuleException extends StoneException {

    public CreateStoneModuleException(Element ownerElement, Throwable cause) {
        super(StoneExceptionStrings.collectCauseMessages(String.format(cannotCreateModule, ownerElement), cause), cause);
    }

}
