package com.github.klee0kai.stone.exceptions;

public class DependencyIncorrectSignatureException extends StoneException {

    public DependencyIncorrectSignatureException(String message) {
        super(message);
    }

    public DependencyIncorrectSignatureException(String message, Throwable e) {
        super(message, e);
    }

}
