package com.github.klee0kai.stone.model;

import javax.lang.model.element.AnnotationMirror;

public class ComponentAnnotation implements Cloneable {

    public static ComponentAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new ComponentAnnotation();
    }


    @Override
    public ComponentAnnotation clone() throws CloneNotSupportedException {
        return (ComponentAnnotation) super.clone();
    }

}
