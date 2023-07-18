package com.github.klee0kai.stone.exceptions;

public class RecurciveProviding extends StoneException {

    public RecurciveProviding(String message) {
        super(message, null);
    }

    public RecurciveProviding(String message, Throwable cause) {
        super(message, cause);
    }

}
