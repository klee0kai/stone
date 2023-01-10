package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ComponentAnnotation implements Cloneable {

    public List<ClassName> qualifiers = new LinkedList<>();

    public List<ClassName> wrapperProviders = new LinkedList<>();

    public static ComponentAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        ComponentAnnotation componentAnnotation = new ComponentAnnotation();

        ExecutableElement qualifiersKey = null;
        ExecutableElement wrappersProvidersKey = null;
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap = annMirror.getElementValues();
        if (elementMap != null) for (ExecutableElement k : elementMap.keySet()) {
            if (Objects.equals(k.getSimpleName().toString(), "qualifiers"))
                qualifiersKey = k;
            if (Objects.equals(k.getSimpleName().toString(), "wrapperProviders"))
                wrappersProvidersKey = k;
        }

        AnnotationValue __qValue = elementMap != null && qualifiersKey != null ? elementMap.get(qualifiersKey) : null;
        if (__qValue != null) {
            List<Object> qualifiers = __qValue.getValue() instanceof List ? (List<Object>) __qValue.getValue() : null;
            for (int i = 0; qualifiers != null && i < qualifiers.size(); i++) {
                if (qualifiers.get(i) == null)
                    continue;
                componentAnnotation.qualifiers.add(ClassNameUtils.typeOf(qualifiers.get(i).toString()));
            }
        }

        AnnotationValue __wrValue = elementMap != null && wrappersProvidersKey != null ? elementMap.get(wrappersProvidersKey) : null;
        if (__wrValue != null) {
            List<Object> wrapperProviders = __wrValue.getValue() instanceof List ? (List<Object>) __wrValue.getValue() : null;
            for (int i = 0; wrapperProviders != null && i < wrapperProviders.size(); i++) {
                if (wrapperProviders.get(i) == null)
                    continue;
                componentAnnotation.wrapperProviders.add(ClassNameUtils.typeOf(wrapperProviders.get(i).toString()));
            }
        }

        return componentAnnotation;
    }


    @Override
    public ComponentAnnotation clone() throws CloneNotSupportedException {
        return (ComponentAnnotation) super.clone();
    }

}
