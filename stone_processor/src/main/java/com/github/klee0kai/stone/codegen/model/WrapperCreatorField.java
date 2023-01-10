package com.github.klee0kai.stone.codegen.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.util.List;

public class WrapperCreatorField {

    public String fieldName;

    public List<ClassName> typeNames;

    public FieldSpec.Builder fieldBuilder;

    public WrapperCreatorField(String fieldName, List<ClassName> typeNames, FieldSpec.Builder fieldBuilder) {
        this.fieldName = fieldName;
        this.typeNames = typeNames;
        this.fieldBuilder = fieldBuilder;
    }

    public boolean isSupport(TypeName typeName) {
        return typeNames.contains(typeName);
    }
}
