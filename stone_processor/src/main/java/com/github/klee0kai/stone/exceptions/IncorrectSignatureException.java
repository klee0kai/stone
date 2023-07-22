package com.github.klee0kai.stone.exceptions;

import javax.lang.model.element.Element;

public class IncorrectSignatureException extends StoneException {

    public IncorrectSignatureException(String message) {
        super(message, null);
    }

    public IncorrectSignatureException(String message, Throwable e) {
        super(message, e);
    }

    public IncorrectSignatureException(String message, Throwable cause, Element element) {
        super(message, cause, element);
    }

    public IncorrectSignatureException(String message, Element element) {
        super(message, null, element);
    }

}
