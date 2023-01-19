package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.annotations.wrappers.WrappersCreator;
import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WrapperCreatorsAnnotation implements Cloneable {

    public List<ClassName> wrappers = new LinkedList<>();

    public static WrapperCreatorsAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;

        WrapperCreatorsAnnotation wrapperCreatorsAnn = new WrapperCreatorsAnnotation();

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
                wrapperCreatorsAnn.wrappers.add(ClassNameUtils.typeOf(qualifiers.get(i).toString()));
            }
        }

        return wrapperCreatorsAnn;
    }


    public static WrapperCreatorsAnnotation findFrom(List<AnnotationSpec> annotationSpecs) {
        AnnotationSpec spec = ListUtils.first(annotationSpecs,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(WrappersCreator.class)));
        if (spec == null)
            return null;
        return new WrapperCreatorsAnnotation();
    }

    @Override
    public WrapperCreatorsAnnotation clone() throws CloneNotSupportedException {
        return (WrapperCreatorsAnnotation) super.clone();
    }
}
