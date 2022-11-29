package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declare scope for gc method.
 * <p>
 * The same behavior for {@link javax.inject.Scope}.
 * Not Follow {@link javax.inject.Scope} documentation.
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.ANNOTATION_TYPE)
public @interface GcScopeAnnotation {
}
