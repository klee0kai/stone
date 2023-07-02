package com.github.klee0kai.stone.helpers.wrap;

import com.squareup.javapoet.TypeName;

public class WrapType {

    public TypeName typeName;

    public boolean isNoCachingWrapper = false;

    public FormatSimple unwrap;

    public FormatSimple wrap;

    public FormatInList inListFormat;

    public boolean isList() {
        return inListFormat != null;
    }
}
