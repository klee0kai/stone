package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.SwitchCache;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class SwitchCacheAnn implements Cloneable, IAnnotation {

    public SwitchCache.CacheType cache;
    public long timeMillis;

    public static SwitchCacheAnn of(SwitchCache ann) {
        if (ann == null)
            return null;
        SwitchCacheAnn sAnn = new SwitchCacheAnn();
        sAnn.timeMillis = ann.timeMillis();
        sAnn.cache = ann.cache();
        return sAnn;
    }

    public static SwitchCacheAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(SwitchCache.class)));
        if (spec == null)
            return null;
        return new SwitchCacheAnn();
    }


    @Override
    public SwitchCacheAnn clone() throws CloneNotSupportedException {
        return (SwitchCacheAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return SwitchCache.class;
    }
}
