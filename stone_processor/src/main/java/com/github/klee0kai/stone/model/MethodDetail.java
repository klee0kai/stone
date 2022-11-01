package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.annotations.BindInstance;
import com.github.klee0kai.stone.annotations.Provide;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MethodDetail implements Cloneable {

    public String methodName;

    public TypeName returnType;

    public List<TypeName> argTypes = new LinkedList<>();

    public SingletonAnnotation singletonAnn;
    public ChangeableSingletonAnnotation changeableAnn;


    public static MethodDetail of(ExecutableElement element) {
        MethodDetail methodDetail = new MethodDetail();
        methodDetail.methodName = element.getSimpleName().toString();
        methodDetail.returnType = TypeName.get(element.getReturnType());
        methodDetail.singletonAnn = SingletonAnnotation.of(element.getAnnotation(Provide.class));
        methodDetail.changeableAnn = ChangeableSingletonAnnotation.of(element.getAnnotation(BindInstance.class));
        for (VariableElement v : element.getParameters()) {
            methodDetail.argTypes.add(TypeName.get(v.asType()));
        }
        return methodDetail;
    }

    /**
     * methods is same if they have same name and params
     *
     * @param methodDetail
     * @return
     */
    public boolean isSameMethod(MethodDetail methodDetail) {
        return methodDetail != null && Objects.equals(this.methodName, methodDetail.methodName) && Objects.equals(argTypes, methodDetail.argTypes);
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
        return Objects.equals(methodName, that.methodName) && Objects.equals(returnType, that.returnType) && Objects.equals(argTypes, that.argTypes) && Objects.equals(singletonAnn, that.singletonAnn) && Objects.equals(changeableAnn, that.changeableAnn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(methodName, returnType, argTypes, singletonAnn, changeableAnn);
    }
}
