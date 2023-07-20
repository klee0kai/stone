package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.inject.Singleton;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class SingletonAnn implements Cloneable, IAnnotation {

    public static SingletonAnn of(Singleton ann) {
        if (ann == null)
            return null;
        return new SingletonAnn();
    }

    public static SingletonAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new SingletonAnn();
    }

    public static SingletonAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Singleton.class)));
        if (spec == null)
            return null;
        return new SingletonAnn();
    }

    @Override
    public SingletonAnn clone() throws CloneNotSupportedException {
        return (SingletonAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Singleton.class;
    }
}
