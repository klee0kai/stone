package com.github.klee0kai.stone.helpers.wrap;

import com.squareup.javapoet.TypeName;

public class WrapType {

    public TypeName typeName;

    public boolean isGeneric = false;

    public FormatSimple unwrap;

    public FormatSimple wrap;

    public FormatInList inListFormat;

    public boolean isList() {
        return inListFormat != null;
    }
}
