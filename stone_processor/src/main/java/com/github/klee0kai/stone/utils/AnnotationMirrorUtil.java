package com.github.klee0kai.stone.utils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

public class AnnotationMirrorUtil {


    public static AnnotationMirror findAnnotationMirror(Element typeElement, Class<?> clazz) {
        String clazzName = clazz.getName();
        for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }


    public static AnnotationValue findValue(AnnotationMirror annotationMirror, String strKey) {
        for (Object k : annotationMirror.getElementValues().keySet()) {
            if (k.toString().equals(strKey)) {
                return annotationMirror.getElementValues().get(k);
            }
        }
        return null;
    }

}
