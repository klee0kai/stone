package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class PrimitiveTypeNonSupportedStoneException extends StoneException {

    public PrimitiveTypeNonSupportedStoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrimitiveTypeNonSupportedStoneException(String message, Throwable cause, Element element) {
        super(message, cause,element);
    }

}
