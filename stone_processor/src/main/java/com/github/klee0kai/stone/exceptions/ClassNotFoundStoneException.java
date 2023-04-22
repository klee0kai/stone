package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.TypeName;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.classNonFoundMes;

public class ClassNotFoundStoneException extends StoneException {

    public ClassNotFoundStoneException(TypeName className, Throwable cause) {
        super(String.format(classNonFoundMes, className.toString()), cause);
    }

    public ClassNotFoundStoneException(String className, Throwable cause) {
        super(String.format(classNonFoundMes, className), cause);
    }

}
