package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public class ObjectNotProvidedException extends StoneException {

    public final TypeName requiredType;

    public final TypeName classWhereShouldBeProvided;
    public final String providingNameOrClass;


    public ObjectNotProvidedException(ClassName requiredType, ClassName classWhereShouldBeProvided, String providingNameOrField) {
        super("Error provide type " + requiredType.simpleName() + ". Required in " + classWhereShouldBeProvided.simpleName() + "." + providingNameOrField);
        this.requiredType = requiredType;
        this.classWhereShouldBeProvided = classWhereShouldBeProvided;
        this.providingNameOrClass = providingNameOrField;
    }

    public ObjectNotProvidedException(TypeName requiredType, TypeName classWhereShouldBeProvided, String providingNameOrField) {
        super("Error provide type " + requiredType + ". Required in " + classWhereShouldBeProvided + "." + providingNameOrField);
        this.requiredType = requiredType;
        this.classWhereShouldBeProvided = classWhereShouldBeProvided;
        this.providingNameOrClass = providingNameOrField;
    }

}
