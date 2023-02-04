package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.util.List;
import java.util.Objects;

public class BindInstanceAnnotation implements Cloneable {

    public BindInstance.CacheType cacheType = BindInstance.CacheType.Soft;

    public static BindInstanceAnnotation of(BindInstance ann) {
        if (ann == null)
            return null;
        BindInstanceAnnotation sAnn = new BindInstanceAnnotation();
        sAnn.cacheType = ann.cache();
        return sAnn;
    }

    public static BindInstanceAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(BindInstance.class)));
        if (spec == null)
            return null;
        return new BindInstanceAnnotation();
    }


    @Override
    public BindInstanceAnnotation clone() throws CloneNotSupportedException {
        return (BindInstanceAnnotation) super.clone();
    }
}
