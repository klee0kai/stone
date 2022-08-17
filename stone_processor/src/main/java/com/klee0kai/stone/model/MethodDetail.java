package com.klee0kai.stone.model;

import com.squareup.javapoet.TypeName;

import java.util.LinkedList;
import java.util.List;

public class MethodDetail {

    public String methodName;

    public TypeName returnType;

    public List<TypeName> argTypes = new LinkedList<>();

    public ItemAnnotation itemAnn;


}
