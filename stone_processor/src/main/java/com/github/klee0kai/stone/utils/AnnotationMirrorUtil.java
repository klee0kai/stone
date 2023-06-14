package com.github.klee0kai.stone.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

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

}
