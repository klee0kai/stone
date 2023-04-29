package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Objects;

public class ProvideAnnotation implements Cloneable {

    public Provide.CacheType cacheType = Provide.CacheType.Soft;

    public static ProvideAnnotation of(Provide ann) {
        if (ann == null)
            return null;
        ProvideAnnotation sAnn = new ProvideAnnotation();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }

    public static ProvideAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Provide.class)));
        if (spec == null)
            return null;
        return new ProvideAnnotation();
    }

    @Override
    public ProvideAnnotation clone() throws CloneNotSupportedException {
        return (ProvideAnnotation) super.clone();
    }

    public boolean isCachingProvideType() {
        return cacheType != Provide.CacheType.Factory;
    }
}
