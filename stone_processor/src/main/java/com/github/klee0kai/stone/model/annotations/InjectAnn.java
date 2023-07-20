package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.closed.types.StListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class InjectAnn implements Cloneable,IAnnotation {

    public static InjectAnn of(Inject ann) {
        if (ann == null)
            return null;
        return new InjectAnn();
    }

    public static InjectAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new InjectAnn();
    }

    public static InjectAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = StListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Inject.class)));
        if (spec == null)
            return null;
        return new InjectAnn();
    }

    @Override
    public InjectAnn clone() throws CloneNotSupportedException {
        return (InjectAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Inject.class;
    }
}
