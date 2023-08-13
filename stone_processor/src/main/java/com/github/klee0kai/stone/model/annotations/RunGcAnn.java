package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.RunGc;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class RunGcAnn implements Cloneable, IAnnotation {


    public static RunGcAnn of(RunGc ann) {
        if (ann == null)
            return null;
        RunGcAnn sAnn = new RunGcAnn();
        return sAnn;
    }

    public static RunGcAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(RunGc.class)));
        if (spec == null)
            return null;
        return new RunGcAnn();
    }


    @Override
    public RunGcAnn clone() throws CloneNotSupportedException {
        return (RunGcAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return RunGc.class;
    }
}
