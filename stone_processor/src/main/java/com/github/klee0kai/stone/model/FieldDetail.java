package com.github.klee0kai.stone.model;

import com.github.klee0kai.stone.closed.types.StListUtils;
import com.github.klee0kai.stone.model.annotations.QualifierAnn;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.github.klee0kai.stone.AnnotationProcessor.allClassesHelper;

/**
 * Collected field details of compile element or field specs.
 * Collect the information you need in one place
 */
public class FieldDetail {

    public String name;

    public TypeName type;

    public Set<QualifierAnn> qualifierAnns = new HashSet<>();

    public boolean injectAnnotation = false;

    /**
     * Take field details for compile variable-element
     *
     * @param p original element
     * @return new FieldDetails object of this element
     */
    public static FieldDetail of(VariableElement p, Element annotationsEl) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.type = TypeName.get(p.asType());
        fieldDetail.name = p.getSimpleName().toString();
        fieldDetail.injectAnnotation = p.getAnnotation(Inject.class) != null;

        for (AnnotationMirror ann : p.getAnnotationMirrors()) {
            String clName = ann.getAnnotationType().toString();
            ClassDetail qualifierAnn = allClassesHelper.findQualifierAnnotation(clName);
            if (qualifierAnn != null) fieldDetail.qualifierAnns.add(QualifierAnn.of(ann));
        }
        if (annotationsEl != null)
            for (AnnotationMirror ann : annotationsEl.getAnnotationMirrors()) {
                String clName = ann.getAnnotationType().toString();
                ClassDetail qualifierAnn = allClassesHelper.findQualifierAnnotation(clName);
                if (qualifierAnn != null) fieldDetail.qualifierAnns.add(QualifierAnn.of(ann));
            }
        return fieldDetail;
    }

    /**
     * Take field details for field spec
     *
     * @param field original element
     * @return new FieldDetails object of this element
     */
    public static FieldDetail of(FieldSpec field) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.name = field.name;
        fieldDetail.type = field.type;
        fieldDetail.injectAnnotation = StListUtils.contains(field.annotations,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Inject.class)));
        return fieldDetail;
    }

    /**
     * Take field details for parameter spec.
     * Annotations not Supported.
     *
     * @param field original element
     * @return new FieldDetail object of this element
     */
    public static FieldDetail of(ParameterSpec field) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.name = field.name;
        fieldDetail.type = field.type;
        fieldDetail.injectAnnotation = StListUtils.contains(field.annotations,
                (inx, ob) -> Objects.equals(ob.type, ClassName.get(Inject.class)));
        return fieldDetail;
    }

    /**
     * Create new field details
     *
     * @param name     name of field
     * @param typeName type of field
     * @return new FieldDetail
     */
    public static FieldDetail simple(String name, TypeName typeName) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.type = typeName;
        fieldDetail.name = name;
        return fieldDetail;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDetail that = (FieldDetail) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
