package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class ClassNotFoundStoneException extends StoneException {

    public ClassNotFoundStoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassNotFoundStoneException(String message, Throwable cause, Element element) {
        super(message, cause, element);
    }

}
