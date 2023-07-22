package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class ObjectNotProvidedException extends StoneException {

    public ObjectNotProvidedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotProvidedException(String message) {
        super(message, null, null);
    }

    public ObjectNotProvidedException(String message, Throwable cause, Element element) {
        super(message, cause, element);
    }

    public ObjectNotProvidedException(String message, Element element) {
        super(message, null, element);
    }
}
