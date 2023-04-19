package com.github.klee0kai.stone.exceptions;

import com.github.klee0kai.stone.utils.ExceptionUtils;

import javax.lang.model.element.Element;

public class CreateStoneModuleException extends StoneException {

    public CreateStoneModuleException(Element ownerElement, Throwable cause) {
        super(ExceptionUtils.collectCauseMessages("Cannot create module: " + ownerElement, cause), cause);
    }

}
