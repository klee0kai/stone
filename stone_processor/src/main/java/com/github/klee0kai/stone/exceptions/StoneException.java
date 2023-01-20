package com.github.klee0kai.stone.exceptions;

public class StoneException extends IllegalStateException {

    public StoneException() {
    }

    public StoneException(String s) {
        super(s);
    }

    public StoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoneException(Throwable cause) {
        super(cause);
    }

}
