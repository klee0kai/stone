package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.ModuleOriginFactory;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class ModuleOriginFactoryAnn implements Cloneable, IAnnotation {

    public static ModuleOriginFactoryAnn of(ModuleOriginFactory ann) {
        if (ann == null)
            return null;
        ModuleOriginFactoryAnn sAnn = new ModuleOriginFactoryAnn();
        return sAnn;
    }

    public static ModuleOriginFactoryAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(ModuleOriginFactory.class)));
        if (spec == null)
            return null;
        return new ModuleOriginFactoryAnn();
    }

    @Override
    public ModuleOriginFactoryAnn clone() throws CloneNotSupportedException {
        return (ModuleOriginFactoryAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return ModuleOriginFactory.class;
    }

}
