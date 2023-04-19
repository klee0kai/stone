package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.inject.Named;
import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.Objects;

public class NamedAnnotation implements Cloneable {

    public static NamedAnnotation of(Named ann) {
        if (ann == null)
            return null;
        return new NamedAnnotation();
    }

    public static NamedAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new NamedAnnotation();
    }

    public static NamedAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Named.class)));
        if (spec == null)
            return null;
        return new NamedAnnotation();
    }

    @Override
    public NamedAnnotation clone() throws CloneNotSupportedException {
        return (NamedAnnotation) super.clone();
    }
}
