package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.AnnotationProcessor;
import com.github.klee0kai.stone.annotations.component.GcAllScope;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.model.annotations.BindInstanceAnnotation;
import com.github.klee0kai.stone.model.annotations.ProtectInjectedAnnotation;
import com.github.klee0kai.stone.model.annotations.ProvideAnnotation;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.*;
import java.util.*;

public class MethodDetail implements Cloneable {

    public String methodName;

    public TypeName returnType;

    public Set<Modifier> modifiers = Collections.emptySet();

    public ElementKind elementKind;

    public List<FieldDetail> args = new LinkedList<>();

    public ProvideAnnotation provideAnnotation;
    public BindInstanceAnnotation bindInstanceAnnotation;

    public ProtectInjectedAnnotation protectInjectedAnnotation;


    public LinkedList<TypeName> gcScopeAnnotations = new LinkedList<>();

    public static MethodDetail simpleName(String methodName) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = methodName;
        return methodDetail;
    }

    public static MethodDetail of(ExecutableElement element) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = element.getSimpleName().toString();
        methodDetail.returnType = TypeName.get(element.getReturnType());
        methodDetail.modifiers = element.getModifiers();
        methodDetail.elementKind = element.getKind();
        methodDetail.provideAnnotation = ProvideAnnotation.of(element.getAnnotation(Provide.class));
        methodDetail.bindInstanceAnnotation = BindInstanceAnnotation.of(element.getAnnotation(BindInstance.class));
        methodDetail.protectInjectedAnnotation = ProtectInjectedAnnotation.of(element.getAnnotation(ProtectInjected.class));
        if (element.getAnnotation(GcAllScope.class) != null) {
            methodDetail.gcScopeAnnotations.add(ClassName.get(GcAllScope.class));
        }
        for (AnnotationMirror ann : element.getAnnotationMirrors()) {
            String clName = ann.getAnnotationType().toString();
            ClassDetail annClDet = AnnotationProcessor.allClassesHelper.findGcScopeAnnotation(clName);
            if (annClDet != null) methodDetail.gcScopeAnnotations.add(annClDet.className);
        }
        for (VariableElement v : element.getParameters())
            methodDetail.args.add(FieldDetail.of(v));
        return methodDetail;
    }

    public boolean isAbstract() {
        return modifiers.contains(Modifier.ABSTRACT);
    }

    /**
     * methods is same if they have same name and params
     *
     * @param methodDetail
     * @return
     */
    public boolean isSameMethod(MethodDetail methodDetail) {
        return methodDetail != null && Objects.equals(this.methodName, methodDetail.methodName) && Objects.equals(args, methodDetail.args);
    }

    @Override
    public MethodDetail clone() throws CloneNotSupportedException {
        return (MethodDetail) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDetail that = (MethodDetail) o;
        return Objects.equals(methodName, that.methodName) && Objects.equals(returnType, that.returnType) && Objects.equals(args, that.args) && Objects.equals(provideAnnotation, that.provideAnnotation) && Objects.equals(bindInstanceAnnotation, that.bindInstanceAnnotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, returnType, args, provideAnnotation, bindInstanceAnnotation);
    }
}
