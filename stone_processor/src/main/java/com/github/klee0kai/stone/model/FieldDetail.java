package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.inject.Inject;
import javax.lang.model.element.VariableElement;
import java.util.Objects;

public class FieldDetail {

    public String name;

    public TypeName type;

    public boolean injectAnnotation = false;

    public static FieldDetail of(VariableElement p) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.type = TypeName.get(p.asType());
        fieldDetail.name = p.getSimpleName().toString();
        fieldDetail.injectAnnotation = p.getAnnotation(Inject.class) != null;
        return fieldDetail;
    }

    public static FieldDetail of(FieldSpec field) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.name = field.name;
        fieldDetail.type = field.type;
        fieldDetail.injectAnnotation = ListUtils.contains(field.annotations,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Inject.class)));
        return fieldDetail;
    }

    /**
     * Annotations not Supported
     */
    public static FieldDetail of(ParameterSpec field) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.name = field.name;
        fieldDetail.type = field.type;
        fieldDetail.injectAnnotation = ListUtils.contains(field.annotations,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Inject.class)));
        return fieldDetail;
    }

    public static FieldDetail simple(String name, TypeName typeName) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.type = typeName;
        fieldDetail.name = name;
        return fieldDetail;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetail that = (FieldDetail) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
