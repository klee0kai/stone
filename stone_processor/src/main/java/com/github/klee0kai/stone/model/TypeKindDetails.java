package com.github.klee0kai.stone.model;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ElementKind;

/**
 * Out class king's enum.
 * Collected class king of compile type or type specs.
 * Collect the information you need in one place
 */
public enum TypeKindDetails {

    /**
     * King of simple java class.
     */
    CLASS,
    /**
     * King of simple java interface.
     */
    INTERFACE,
    /**
     * Java enum type
     */
    ENUM,
    /**
     * Java annotation type
     */
    ANNOTATION;

    /**
     * Take class king details from compile type element
     *
     * @param elementKind original element
     * @return new TypeKindDetails object of this element
     */
    public static TypeKindDetails of(ElementKind elementKind) {
        if (elementKind == ElementKind.ANNOTATION_TYPE) {
            return ANNOTATION;
        } else if (elementKind == ElementKind.INTERFACE) {
            return INTERFACE;
        } else if (elementKind == ElementKind.ENUM) {
            return ENUM;
        } else if (elementKind == ElementKind.CLASS) {
            return CLASS;
        } else {
            return null;
        }
    }

    /**
     * Take class king details from type specs.
     *
     * @param king original element
     * @return new TypeKindDetails object of this element
     */
    public static TypeKindDetails of(TypeSpec.Kind king) {
        if (king == TypeSpec.Kind.ANNOTATION) {
            return ANNOTATION;
        } else if (king == TypeSpec.Kind.INTERFACE) {
            return INTERFACE;
        } else if (king == TypeSpec.Kind.ENUM) {
            return ENUM;
        } else if (king == TypeSpec.Kind.CLASS) {
            return CLASS;
        } else {
            return null;
        }
    }
}


