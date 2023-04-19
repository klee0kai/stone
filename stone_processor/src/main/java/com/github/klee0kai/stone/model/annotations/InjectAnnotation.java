package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.Objects;

public class InjectAnnotation implements Cloneable {

    public static InjectAnnotation of(Inject ann) {
        if (ann == null)
            return null;
        return new InjectAnnotation();
    }

    public static InjectAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new InjectAnnotation();
    }

    public static InjectAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Inject.class)));
        if (spec == null)
            return null;
        return new InjectAnnotation();
    }

    @Override
    public InjectAnnotation clone() throws CloneNotSupportedException {
        return (InjectAnnotation) super.clone();
    }
}
