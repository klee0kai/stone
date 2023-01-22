package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.Objects;

public class ModuleAnnotation implements Cloneable {


    public static ModuleAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new ModuleAnnotation();
    }

    public static ModuleAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Module.class)));
        if (spec == null)
            return null;
        return new ModuleAnnotation();
    }

    @Override
    public ModuleAnnotation clone() throws CloneNotSupportedException {
        return (ModuleAnnotation) super.clone();
    }
}
