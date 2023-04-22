package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.TypeName;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.errorProvideType;

public class ObjectNotProvidedException extends StoneException {

    public final TypeName requiredType;

    public final TypeName classWhereShouldBeProvided;
    public final String providingNameOrClass;


    public ObjectNotProvidedException(TypeName requiredType, TypeName classWhereShouldBeProvided, String providingNameOrField) {
        super(String.format(errorProvideType, requiredType.toString(), classWhereShouldBeProvided.toString(), providingNameOrField));
        this.requiredType = requiredType;
        this.classWhereShouldBeProvided = classWhereShouldBeProvided;
        this.providingNameOrClass = providingNameOrField;
    }

}
