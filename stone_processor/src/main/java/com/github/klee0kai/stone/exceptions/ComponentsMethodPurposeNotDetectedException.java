package com.github.klee0kai.stone.exceptions;

import com.github.klee0kai.stone.model.MethodDetail;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.TypeName;

import static com.github.klee0kai.stone.exceptions.StoneExceptionStrings.methodPurposeNonDetected;

public class ComponentsMethodPurposeNotDetectedException extends StoneException {

    public final TypeName classWhereShouldBeProvided;
    public final MethodDetail method;

    public ComponentsMethodPurposeNotDetectedException(TypeName classWhereShouldBeProvided, MethodDetail method) {
        super(String.format(methodPurposeNonDetected, method.methodName, ClassNameUtils.simpleName(classWhereShouldBeProvided)));
        this.classWhereShouldBeProvided = classWhereShouldBeProvided;
        this.method = method;
    }


}
