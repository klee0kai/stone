package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.model.annotations.BindInstanceAnnotation;
import com.github.klee0kai.stone.model.annotations.ProvideAnnotation;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.*;

public class MethodDetail implements Cloneable {

    public String methodName;

    public TypeName returnType;

    public Set<Modifier> modifiers = Collections.emptySet();

    public ElementKind elementKind;

    public List<FieldDetail> args = new LinkedList<>();

    public ProvideAnnotation provideAnnotation;
    public BindInstanceAnnotation bindInstanceAnnotation;


    public static MethodDetail of(ExecutableElement element) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = element.getSimpleName().toString();
        methodDetail.returnType = TypeName.get(element.getReturnType());
        methodDetail.modifiers = element.getModifiers();
        methodDetail.elementKind = element.getKind();
        methodDetail.provideAnnotation = ProvideAnnotation.of(element.getAnnotation(Provide.class));
        methodDetail.bindInstanceAnnotation = BindInstanceAnnotation.of(element.getAnnotation(BindInstance.class));
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
