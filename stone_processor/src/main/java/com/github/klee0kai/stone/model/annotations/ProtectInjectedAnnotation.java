package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Objects;

public class ProtectInjectedAnnotation implements Cloneable {

    public long timeMillis;

    public static ProtectInjectedAnnotation of(ProtectInjected ann) {
        if (ann == null)
            return null;
        ProtectInjectedAnnotation sAnn = new ProtectInjectedAnnotation();
        sAnn.timeMillis = ann.timeMillis();
        return sAnn;
    }

    public static ProtectInjectedAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(ProtectInjected.class)));
        if (spec == null)
            return null;
        return new ProtectInjectedAnnotation();
    }

    @Override
    public ProtectInjectedAnnotation clone() throws CloneNotSupportedException {
        return (ProtectInjectedAnnotation) super.clone();
    }
}
