package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.ProtectInjected;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class ProtectInjectedAnn implements Cloneable, IAnnotation {

    public long timeMillis;

    public static ProtectInjectedAnn of(ProtectInjected ann) {
        if (ann == null)
            return null;
        ProtectInjectedAnn sAnn = new ProtectInjectedAnn();
        sAnn.timeMillis = ann.timeMillis();
        return sAnn;
    }

    public static ProtectInjectedAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(ProtectInjected.class)));
        if (spec == null)
            return null;
        return new ProtectInjectedAnn();
    }

    @Override
    public ProtectInjectedAnn clone() throws CloneNotSupportedException {
        return (ProtectInjectedAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return ProtectInjected.class;
    }
}
