package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.inject.Named;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class NamedAnn implements Cloneable, IAnnotation {

    public static NamedAnn of(Named ann) {
        if (ann == null)
            return null;
        return new NamedAnn();
    }

    public static NamedAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new NamedAnn();
    }

    public static NamedAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Named.class)));
        if (spec == null)
            return null;
        return new NamedAnn();
    }

    @Override
    public NamedAnn clone() throws CloneNotSupportedException {
        return (NamedAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Named.class;
    }
}
