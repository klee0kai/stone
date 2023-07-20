package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.component.Init;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class InitAnn implements Cloneable, IAnnotation {

    public static InitAnn of(Init ann) {
        if (ann == null)
            return null;
        return new InitAnn();
    }

    public static InitAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new InitAnn();
    }

    public static InitAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = StListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Init.class)));
        if (spec == null)
            return null;
        return new InitAnn();
    }

    @Override
    public InitAnn clone() throws CloneNotSupportedException {
        return (InitAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Init.class;
    }
}
