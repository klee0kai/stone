package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * declare scope for gc method.
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.ANNOTATION_TYPE)
public @interface GcScopeAnnotation {
}