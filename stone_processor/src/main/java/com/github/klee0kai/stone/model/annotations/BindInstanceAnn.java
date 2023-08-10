package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.module.BindInstance;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.PrimitiveTypeNonSupportedStoneException;
import com.github.klee0kai.stone.utils.AnnotationMirrorUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BindInstanceAnn implements Cloneable, IAnnotation {

    public BindInstance.CacheType cacheType = BindInstance.CacheType.Soft;
    public LinkedList<ClassName> alternatives = new LinkedList<>();

    public static BindInstanceAnn find(Element element) {
        if (element == null) return null;
        AnnotationMirror annMirror = AnnotationMirrorUtil.findAnnotationMirror(element, BindInstance.class);
        BindInstance ann = element.getAnnotation(BindInstance.class);
        if (annMirror == null || ann == null) return null;

        BindInstanceAnn sAnn = new BindInstanceAnn();
        sAnn.cacheType = ann.cache();
        try {
            sAnn.alternatives.addAll(AnnotationMirrorUtil.getClassesFrom(annMirror, "alternatives"));
        } catch (Exception e) {
            if (e instanceof PrimitiveTypeNonSupportedStoneException) {
                throw new IncorrectSignatureException("Primitive types non supported for BindInstance", e);
            }
            throw e;
        }
        return sAnn;
    }

    public static BindInstanceAnn findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(BindInstance.class)));
        if (spec == null)
            return null;
        return new BindInstanceAnn();
    }


    @Override
    public BindInstanceAnn clone() throws CloneNotSupportedException {
        return (BindInstanceAnn) super.clone();
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return BindInstance.class;
    }
}
