package com.github.klee0kai.stone.exceptions;

import com.github.klee0kai.stone.utils.ExceptionUtils;

import javax.lang.model.element.Element;

public class CreateStoneComponentException extends StoneException {

    public CreateStoneComponentException(Element ownerElement, Throwable cause) {
        super(ExceptionUtils.collectCauseMessages("Cannot create component: " + ownerElement, cause), cause);
    }

}
