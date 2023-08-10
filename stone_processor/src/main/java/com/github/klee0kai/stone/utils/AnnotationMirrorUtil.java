package com.github.klee0kai.stone.utils;

import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Annotation mirror's util
 */
public class AnnotationMirrorUtil {


    /**
     * Find annotation mirror on element by the type
     *
     * @param typeElement compile code element
     * @param clazz       annotation class
     * @return annotation mirror if exist
     */
    public static AnnotationMirror findAnnotationMirror(Element typeElement, Class<?> clazz) {
        String clazzName = clazz.getName();
        for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }


    /**
     * Find value for annotation mirror by the name of field
     *
     * @param annotationMirror annotation mirror
     * @param strKey           field's name
     * @return value if exist
     */
    public static AnnotationValue findValue(AnnotationMirror annotationMirror, String strKey) {
        for (Object k : annotationMirror.getElementValues().keySet()) {
            if (k.toString().equals(strKey)) {
                return annotationMirror.getElementValues().get(k);
            }
        }
        return null;
    }


    public static List<ClassName> getClassesFrom(AnnotationMirror annMirror, String methodName) {
        List<ClassName> classes = new LinkedList<>();
        ExecutableElement wrappersKey = null;
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap = annMirror.getElementValues();
        if (elementMap != null) for (ExecutableElement k : elementMap.keySet())
            if (Objects.equals(k.getSimpleName().toString(), methodName))
                wrappersKey = k;

        AnnotationValue __wrValue = elementMap != null && wrappersKey != null ? elementMap.get(wrappersKey) : null;
        if (__wrValue != null) {
            List<Object> wrappers = __wrValue.getValue() instanceof List ? (List<Object>) __wrValue.getValue() : null;
            for (int i = 0; wrappers != null && i < wrappers.size(); i++) {
                if (wrappers.get(i) == null)
                    continue;
                classes.add(ClassNameUtils.classNameOf(wrappers.get(i).toString()));
            }
        }
        return classes;
    }

}
