package com.github.klee0kai.stone.exceptions;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.primitiveTypeNonSupported;

public class PrimitiveTypeNonSupportedStoneException extends StoneException {

    public PrimitiveTypeNonSupportedStoneException(String className, Throwable cause) {
        super(String.format(primitiveTypeNonSupported, className), cause);
    }

}
