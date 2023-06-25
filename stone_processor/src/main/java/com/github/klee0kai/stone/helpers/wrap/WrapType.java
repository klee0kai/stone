package com.github.klee0kai.stone.helpers.wrap;

import com.github.klee0kai.stone.helpers.codebuilder.SmartCode;
import com.squareup.javapoet.TypeName;

import java.util.function.Function;

public class WrapType {

    public TypeName typeName;

    public boolean isGeneric;

    public Function<SmartCode, SmartCode> wrap;
    public Function<SmartCode, SmartCode> unwrap;

}
