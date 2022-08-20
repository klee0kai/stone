package com.github.klee0kai.stone.model;

import javax.lang.model.element.AnnotationMirror;

public class ModuleAnnotation implements Cloneable {


    public static ModuleAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        return new ModuleAnnotation();
    }

    @Override
    public ModuleAnnotation clone() throws CloneNotSupportedException {
        return (ModuleAnnotation) super.clone();
    }
}
