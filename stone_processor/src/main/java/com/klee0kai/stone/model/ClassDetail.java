package com.klee0kai.stone.model;

import com.squareup.javapoet.ClassName;

import java.util.LinkedList;
import java.util.List;

public class ClassDetail {

    public ClassName classType;

    public ClassName superClassType;

    public ComponentAnnotation componentAnn;
    public ModuleAnnotation moduleAnn;

    public List<MethodDetail> methods = new LinkedList<>();

}
