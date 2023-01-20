package com.github.klee0kai.stone.exceptions;

import com.squareup.javapoet.ClassName;

public class InjectNotProvided extends StoneException {

    public final ClassName className;
    public final String fieldName;

    public InjectNotProvided(ClassName className, String fieldName) {
        super("Error inject " + className.simpleName() + "." + fieldName);
        this.className = className;
        this.fieldName = fieldName;
    }


}
