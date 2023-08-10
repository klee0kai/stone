package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.PrimitiveTypeNonSupportedStoneException;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ComponentAnn implements Cloneable, IAnnotation {

    public List<ClassName> identifiers = new LinkedList<>();

    public List<ClassName> wrapperProviders = new LinkedList<>();

    public static ComponentAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        ComponentAnn componentAnn = new ComponentAnn();

        try {
            componentAnn.identifiers = AnnotationMirrorUtil.getClassesFrom(annMirror, "identifiers");
        } catch (Exception e) {
            if (e instanceof PrimitiveTypeNonSupportedStoneException) {
                throw new IncorrectSignatureException("Primitive types non supported for Component's identifiers", e);
            }
            throw e;
        }
        try{
            componentAnn.wrapperProviders = AnnotationMirrorUtil.getClassesFrom(annMirror, "wrapperProviders");
        }catch (Exception e){
            if (e instanceof PrimitiveTypeNonSupportedStoneException) {
                throw new IncorrectSignatureException("Primitive types non supported for Component's WrapperProviders", e);
            }
            throw e;
        }

        return componentAnn;
    }


    public static ComponentAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Component.class)));
        if (spec == null)
            return null;
        return new ComponentAnn();
    }

    @Override
    public ComponentAnn clone() throws CloneNotSupportedException {
        return (ComponentAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return Component.class;
    }
}
