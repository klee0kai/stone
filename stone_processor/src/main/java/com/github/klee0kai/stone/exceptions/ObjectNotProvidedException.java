package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.TypeName;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.errorProvideType;

public class ObjectNotProvidedException extends StoneException {


    public ObjectNotProvidedException(TypeName requiredType, TypeName classWhereShouldBeProvided, String providingNameOrField) {
        super(String.format(errorProvideType, requiredType.toString(), classWhereShouldBeProvided.toString(), providingNameOrField));
    }

    public ObjectNotProvidedException(String message) {
        super(message);
    }


}
