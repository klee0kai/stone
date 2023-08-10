package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
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

public class WrapperCreatorsAnn implements Cloneable, IAnnotation {

    public List<ClassName> wrappers = new LinkedList<>();

    public static WrapperCreatorsAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;

        WrapperCreatorsAnn wrapperCreatorsAnn = new WrapperCreatorsAnn();
        try {
            wrapperCreatorsAnn.wrappers.addAll(AnnotationMirrorUtil.getClassesFrom(annMirror, "wrappers"));
        } catch (Exception e) {
            if (e instanceof PrimitiveTypeNonSupportedStoneException) {
                throw new IncorrectSignatureException("Primitive types non supported for Component's Wrappers", e);
            }
            throw e;
        }
        return wrapperCreatorsAnn;
    }


    public static WrapperCreatorsAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(WrappersCreator.class)));
        if (spec == null)
            return null;
        return new WrapperCreatorsAnn();
    }

    @Override
    public WrapperCreatorsAnn clone() throws CloneNotSupportedException {
        return (WrapperCreatorsAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return WrappersCreator.class;
    }
}
