package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.closed.types.StListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class ModuleAnn implements Cloneable, IAnnotation {

    public static ModuleAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new ModuleAnn();
    }

    public static ModuleAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = StListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Module.class)));
        if (spec == null)
            return null;
        return new ModuleAnn();
    }

    @Override
    public ModuleAnn clone() throws CloneNotSupportedException {
        return (ModuleAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Module.class;
    }
}
