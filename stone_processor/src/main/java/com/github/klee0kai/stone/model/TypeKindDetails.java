package com.github.klee0kai.stone.model;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.ElementKind;

public enum TypeKindDetails {
    CLASS,
    INTERFACE,
    ENUM,
    ANNOTATION;

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


