package com.github.klee0kai.stone.model.annotations;

import com.github.klee0kai.stone.closed.types.ListUtils;
import com.github.klee0kai.stone.model.ClassDetail;
import com.github.klee0kai.stone.model.MethodDetail;
import com.squareup.javapoet.ClassName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

public class QualifierAnn implements Cloneable, IAnnotation {


    public String qualifierClStr = null;

    public Map<String, Object> values = new HashMap<>();

    public static QualifierAnn custom(String custom) {
        QualifierAnn qualifier = new QualifierAnn();
        qualifier.qualifierClStr = custom;
        return qualifier;
    }

    public static QualifierAnn of(AnnotationMirror annMirror) {
        if (annMirror == null)
            return null;
        QualifierAnn qualifier = new QualifierAnn();
        qualifier.qualifierClStr = annMirror.getAnnotationType().toString();
        Map<? extends ExecutableElement, ? extends AnnotationValue> elementMap = annMirror.getElementValues();
        if (elementMap != null) for (ExecutableElement k : elementMap.keySet())
            qualifier.values.putIfAbsent(k.getSimpleName().toString(), elementMap.get(k).getValue());

        ClassDetail annCl = allClassesHelper.tryFindForType(ClassName.get(annMirror.getAnnotationType()));
        if (annCl != null) {
            for (MethodDetail m : annCl.getAllMethods(false, false)) {
                if (m.defValue != null) qualifier.values.putIfAbsent(m.methodName, m.defValue);
            }
        }

        return qualifier;
    }

    @Override
    public Class<? extends Annotation> originalAnn() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualifierAnn that = (QualifierAnn) o;
        return Objects.equals(qualifierClStr, that.qualifierClStr) && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(qualifierClStr, values);
    }

    @Override
    public QualifierAnn clone() throws CloneNotSupportedException {
        return (QualifierAnn) super.clone();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("@");
        builder.append(qualifierClStr);
        if (!values.isEmpty()) {
            builder.append("(")
                    .append(String.join(",", ListUtils.format(values.values(), Object::toString)))
                    .append(")");

        }
        return builder.toString();
    }
}
