package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WrapperCreatorsAnn implements Cloneable, IAnnotation {

    public List<ClassName> wrappers = new LinkedList<>();

    public static WrapperCreatorsAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;

        WrapperCreatorsAnn wrapperCreatorsAnn = new WrapperCreatorsAnn();

        ExecutableElement wrappersKey = null;
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap = annMirror.getElementValues();
        if (elementMap != null) for (ExecutableElement k : elementMap.keySet())
            if (Objects.equals(k.getSimpleName().toString(), "wrappers"))
                wrappersKey = k;

        AnnotationValue __wrValue = elementMap != null && wrappersKey != null ? elementMap.get(wrappersKey) : null;
        if (__wrValue != null) {
            List<Object> qualifiers = __wrValue.getValue() instanceof List ? (List<Object>) __wrValue.getValue() : null;
            for (int i = 0; qualifiers != null && i < qualifiers.size(); i++) {
                if (qualifiers.get(i) == null)
                    continue;
                wrapperCreatorsAnn.wrappers.add(ClassNameUtils.classNameOf(qualifiers.get(i).toString()));
            }
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
