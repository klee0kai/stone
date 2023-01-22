package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Objects;

public class SwitchCacheAnnotation implements Cloneable {

    public SwitchCache.CacheType cache;
    public long timeMillis;

    public static SwitchCacheAnnotation of(SwitchCache ann) {
        if (ann == null)
            return null;
        SwitchCacheAnnotation sAnn = new SwitchCacheAnnotation();
        sAnn.timeMillis = ann.timeMillis();
        sAnn.cache = ann.cache();
        return sAnn;
    }

    public static SwitchCacheAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(SwitchCache.class)));
        if (spec == null)
            return null;
        return new SwitchCacheAnnotation();
    }


    @Override
    public SwitchCacheAnnotation clone() throws CloneNotSupportedException {
        return (SwitchCacheAnnotation) super.clone();
    }
}
