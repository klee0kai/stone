package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.inject.Singleton;
import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.Objects;

public class SingletonAnnotation implements Cloneable {

    public static SingletonAnnotation of(Singleton ann) {
        if (ann == null)
            return null;
        return new SingletonAnnotation();
    }

    public static SingletonAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new SingletonAnnotation();
    }

    public static SingletonAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Singleton.class)));
        if (spec == null)
            return null;
        return new SingletonAnnotation();
    }

    @Override
    public SingletonAnnotation clone() throws CloneNotSupportedException {
        return (SingletonAnnotation) super.clone();
    }
}
