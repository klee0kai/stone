package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class StoneException extends IllegalStateException {

    public Element errorElement = null;

    public StoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoneException(String message, Throwable cause, Element element) {
        this(message, cause);
        this.errorElement = element;
    }

    public Element findErrorElement() {
        Element sourceElement = null;
        if (getCause() instanceof StoneException) {
            sourceElement = ((StoneException) getCause()).findErrorElement();
        }
        if (sourceElement == null) sourceElement = errorElement;
        return sourceElement;
    }


}
