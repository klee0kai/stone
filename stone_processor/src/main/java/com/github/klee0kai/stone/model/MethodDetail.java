package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.Singletone;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.LinkedList;
import java.util.List;

public class MethodDetail implements Cloneable {

    public String methodName;

    public TypeName returnType;

    public List<TypeName> argTypes = new LinkedList<>();

    public SingletonAnnotation singletonAnn;


    public static MethodDetail of(ExecutableElement element) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = element.getSimpleName().toString();
        methodDetail.returnType = TypeName.get(element.getReturnType());
        methodDetail.singletonAnn = SingletonAnnotation.of(element.getAnnotation(Singletone.class));
        for (VariableElement v : element.getParameters()) {
            methodDetail.argTypes.add(TypeName.get(v.asType()));
        }
        return methodDetail;
    }

    @Override
    public MethodDetail clone() throws CloneNotSupportedException {
        return (MethodDetail) super.clone();
    }
}
