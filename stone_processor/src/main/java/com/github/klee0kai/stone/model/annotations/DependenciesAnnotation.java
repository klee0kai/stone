package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.dependencies.Dependencies;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.util.List;
import java.util.Objects;

public class DependenciesAnnotation implements Cloneable {

    public static DependenciesAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new DependenciesAnnotation();
    }

    public static DependenciesAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Dependencies.class)));
        if (spec == null)
            return null;
        return new DependenciesAnnotation();
    }

    @Override
    public DependenciesAnnotation clone() throws CloneNotSupportedException {
        return (DependenciesAnnotation) super.clone();
    }
}
