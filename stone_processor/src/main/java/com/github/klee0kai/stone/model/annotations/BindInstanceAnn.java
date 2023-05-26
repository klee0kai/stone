package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class BindInstanceAnn implements Cloneable, IAnnotation {

    public BindInstance.CacheType cacheType = BindInstance.CacheType.Soft;

    public static BindInstanceAnn of(BindInstance ann) {
        if (ann == null)
            return null;
        BindInstanceAnn sAnn = new BindInstanceAnn();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }

    public static BindInstanceAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(BindInstance.class)));
        if (spec == null)
            return null;
        return new BindInstanceAnn();
    }


    @Override
    public BindInstanceAnn clone() throws CloneNotSupportedException {
        return (BindInstanceAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return BindInstance.class;
    }
}
