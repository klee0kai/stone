package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class RecursiveProviding extends StoneException {

    public RecursiveProviding(String message) {
        super(message, null);
    }

    public RecursiveProviding(String message, Throwable cause) {
        super(message, cause);
    }

    public RecursiveProviding(String message, Throwable cause, Element element) {
        super(message, cause,element);
    }

}
