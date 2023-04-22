package com.github.klee0kai.stone.exceptions;

import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.methodPurposeNonDetected;

public class ComponentsMethodPurposeNotDetectedException extends StoneException {

    public final TypeName classWhereShouldBeProvided;
    public final MethodDetail method;

    public ComponentsMethodPurposeNotDetectedException(ClassName classWhereShouldBeProvided, MethodDetail method) {
        super(String.format(methodPurposeNonDetected, method.methodName, classWhereShouldBeProvided.simpleName()));
        this.classWhereShouldBeProvided = classWhereShouldBeProvided;
        this.method = method;
    }


}
