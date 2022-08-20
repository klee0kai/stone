package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Module;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.github.klee0kai.stone.annotations.Component;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.LinkedList;
import java.util.List;

public class ClassDetail implements Cloneable {

    public ClassName classType;


    public ComponentAnnotation componentAnn;
    public ModuleAnnotation moduleAnn;

    public List<MethodDetail> methods = new LinkedList<>();

    public List<ClassName> superTypes = new LinkedList<>();


    public static ClassDetail of(TypeElement owner) {
        ClassDetail classDetail = new ClassDetail();
        List<? extends Element> children = owner.getEnclosedElements();
        classDetail.classType = ClassNameUtils.typeOf(owner.getQualifiedName().toString());
        classDetail.componentAnn = ComponentAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Component.class));
        classDetail.moduleAnn = ModuleAnnotation.of(AnnotationMirrorUtil.findAnnotationMirror(owner, Module.class));
        for (Element el : owner.getEnclosedElements()) {
            if (!(el instanceof ExecutableElement))
                continue;
            classDetail.methods.add(MethodDetail.of((ExecutableElement) el));
        }

        return classDetail;
    }

    @Override
    public ClassDetail clone() throws CloneNotSupportedException {
        return (ClassDetail) super.clone();
    }
}
