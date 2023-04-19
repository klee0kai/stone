package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.TypeName;

public class ClassNotFoundException extends StoneException {

    public ClassNotFoundException(TypeName className, Throwable cause) {
        super("Class not found: " + className.toString() + ".\nTry import class directly", cause);
    }

}
