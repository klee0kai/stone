package com.github.klee0kai.stone.exceptions;

public class ModuleIncorrectSignatureException extends StoneException {

    public ModuleIncorrectSignatureException(String message) {
        super(message);
    }

    public ModuleIncorrectSignatureException(String message, Throwable e) {
        super(message, e);
    }

}
