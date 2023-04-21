package com.github.klee0kai.stone.exceptions;

public class PrimitiveTypeNonSupportedStoneException extends StoneException {

    public PrimitiveTypeNonSupportedStoneException(String className, Throwable cause) {
        super("Primitive type non supported: " + className + " ", cause);
    }

}
