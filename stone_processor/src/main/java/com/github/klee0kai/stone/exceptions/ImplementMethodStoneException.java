package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class ImplementMethodStoneException extends StoneException {

    public ImplementMethodStoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImplementMethodStoneException(String message, Throwable cause, Element element) {
        super(message, cause, element);
    }

}
