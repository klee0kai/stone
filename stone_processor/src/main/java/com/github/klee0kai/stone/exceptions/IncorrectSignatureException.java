package com.github.klee0kai.stone.exceptions;

public class IncorrectSignatureException extends StoneException {

    public IncorrectSignatureException(String message) {
        super(message, null);
    }

    public IncorrectSignatureException(String message, Throwable e) {
        super(message, e);
    }

}
