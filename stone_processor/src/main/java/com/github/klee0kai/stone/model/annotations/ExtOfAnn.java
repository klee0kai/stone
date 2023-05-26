package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.component.ExtendOf;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class ExtOfAnn implements Cloneable, IAnnotation {

    public static ExtOfAnn of(ExtendOf ann) {
        if (ann == null)
            return null;
        return new ExtOfAnn();
    }

    public static ExtOfAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new ExtOfAnn();
    }

    public static ExtOfAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(ExtendOf.class)));
        if (spec == null)
            return null;
        return new ExtOfAnn();
    }

    @Override
    public ExtOfAnn clone() throws CloneNotSupportedException {
        return (ExtOfAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return ExtendOf.class;
    }
}
