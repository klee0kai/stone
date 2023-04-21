package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.TypeName;

public class ClassNotFoundStoneException extends StoneException {

    public ClassNotFoundStoneException(TypeName className, Throwable cause) {
        super("Class not found: " + className.toString() + "\nTry import class directly", cause);
    }

    public ClassNotFoundStoneException(String className, Throwable cause) {
        super("Class not found: " + className + "\nTry import class directly", cause);
    }

}
