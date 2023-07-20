package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone._hidden_.types.ListUtils;
import com.github.klee0kai.stone.annotations.component.Component;
import com.github.klee0kai.stone.exceptions.IncorrectSignatureException;
import com.github.klee0kai.stone.exceptions.PrimitiveTypeNonSupportedStoneException;
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

public class ComponentAnn implements Cloneable, IAnnotation {

    public List<ClassName> qualifiers = new LinkedList<>();

    public List<ClassName> wrapperProviders = new LinkedList<>();

    public static ComponentAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        ComponentAnn componentAnn = new ComponentAnn();

        ExecutableElement qualifiersKey = null;
        ExecutableElement wrappersProvidersKey = null;
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap = annMirror.getElementValues();
        if (elementMap != null) for (ExecutableElement k : elementMap.keySet()) {
            if (Objects.equals(k.getSimpleName().toString(), "qualifiers"))
                qualifiersKey = k;
            if (Objects.equals(k.getSimpleName().toString(), "wrapperProviders"))
                wrappersProvidersKey = k;
        }

        try {
            AnnotationValue __qValue = elementMap != null && qualifiersKey != null ? elementMap.get(qualifiersKey) : null;
            if (__qValue != null) {
                List<Object> qualifiers = __qValue.getValue() instanceof List ? (List<Object>) __qValue.getValue() : null;
                for (int i = 0; qualifiers != null && i < qualifiers.size(); i++) {
                    if (qualifiers.get(i) == null)
                        continue;
                    componentAnn.qualifiers.add(ClassNameUtils.classNameOf(qualifiers.get(i).toString()));
                }
            }
        } catch (Exception e) {
            if (e instanceof PrimitiveTypeNonSupportedStoneException) {
                throw new IncorrectSignatureException("Primitive types non supported for Component's qualifiers", e);
            }
            throw e;
        }

        try {
            AnnotationValue __wrValue = elementMap != null && wrappersProvidersKey != null ? elementMap.get(wrappersProvidersKey) : null;
            if (__wrValue != null) {
                List<Object> wrapperProviders = __wrValue.getValue() instanceof List ? (List<Object>) __wrValue.getValue() : null;
                for (int i = 0; wrapperProviders != null && i < wrapperProviders.size(); i++) {
                    if (wrapperProviders.get(i) == null)
                        continue;
                    componentAnn.wrapperProviders.add(ClassNameUtils.classNameOf(wrapperProviders.get(i).toString()));
                }
            }
        } catch (Exception e) {
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
