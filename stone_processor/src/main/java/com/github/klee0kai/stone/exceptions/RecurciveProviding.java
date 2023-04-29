package com.github.klee0kai.stone.exceptions;

public class RecurciveProviding extends StoneException {

    public RecurciveProviding() {
    }

    public RecurciveProviding(String s) {
        super(s);
    }

    public RecurciveProviding(String message, Throwable cause) {
        super(message, cause);
    }

    public RecurciveProviding(Throwable cause) {
        super(cause);
    }

}
