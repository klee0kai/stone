package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

public class DependenciesAnn implements Cloneable, IAnnotation {

    public static DependenciesAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new DependenciesAnn();
    }

    public static DependenciesAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Dependencies.class)));
        if (spec == null)
            return null;
        return new DependenciesAnn();
    }

    @Override
    public DependenciesAnn clone() throws CloneNotSupportedException {
        return (DependenciesAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Dependencies.class;
    }
}
