package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.utils.ClassNameUtils;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.util.*;

public class ComponentAnnotation implements Cloneable {

    public List<ClassName> qualifiers = new LinkedList<>();

    public static ComponentAnnotation of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        ComponentAnnotation componentAnnotation = new ComponentAnnotation();

        ExecutableElement qualifiersKey = null;
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap = annMirror.getElementValues();
        if (elementMap != null) for (ExecutableElement k : elementMap.keySet())
            if (Objects.equals(k.getSimpleName().toString(), "qualifiers"))
                qualifiersKey = k;

        AnnotationValue __qValue = elementMap != null && qualifiersKey != null ? elementMap.get(qualifiersKey) : null;
        if (__qValue != null) {
            List<Object> qualifiers = __qValue.getValue() instanceof List ? (List<Object>) __qValue.getValue() : null;
            for (int i = 0; qualifiers != null && i < qualifiers.size(); i++) {
                if (qualifiers.get(i)==null)
                    continue;
                componentAnnotation.qualifiers.add(ClassNameUtils.typeOf(qualifiers.get(i).toString()));
            }
        }

        return componentAnnotation;
    }


    @Override
    public ComponentAnnotation clone() throws CloneNotSupportedException {
        return (ComponentAnnotation) super.clone();
    }

}
