package com.github.klee0kai.stone.exceptions;

import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

public class ComponentsMethodPurposeNotDetectedException extends StoneException {


    public final TypeName classWhereShouldBeProvided;
    public final MethodDetail method;

    private static final String componentMethodsMethods = "Supported component's methods:\n" +
            "- Module provide method. Module class should be annotated as @Module\n";

    public ComponentsMethodPurposeNotDetectedException(ClassName classWhereShouldBeProvided, MethodDetail method) {
        super("What is purpose for Method '" + method.methodName + "'. Declared in "
                + classWhereShouldBeProvided.simpleName() + "\n" + componentMethodsMethods);
        this.classWhereShouldBeProvided = classWhereShouldBeProvided;
        this.method = method;
    }


}
