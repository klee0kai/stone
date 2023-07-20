package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class ProvideAnn implements Cloneable, IAnnotation {

    public Provide.CacheType cacheType = Provide.CacheType.Soft;

    public static ProvideAnn of(Provide ann) {
        if (ann == null)
            return null;
        ProvideAnn sAnn = new ProvideAnn();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }

    public static ProvideAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = StListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Provide.class)));
        if (spec == null)
            return null;
        return new ProvideAnn();
    }

    @Override
    public ProvideAnn clone() throws CloneNotSupportedException {
        return (ProvideAnn) super.clone();
    }

    public boolean isCachingProvideType() {
        return cacheType != Provide.CacheType.Factory;
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Provide.class;
    }
}
