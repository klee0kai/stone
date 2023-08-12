package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation announcing new scopes for garbage collection and changing the caching method.
 * Announce new scopes in the following way.
 *
 * <pre>{@code
 *    ㅤ@GcScopeAnnotation
 *    ㅤ@Retention(RUNTIME)
 *    ㅤ@Target(METHOD)
 *     public @interface GcPlanetScope {
 *     }
 * }</pre>
 * Instead of this annotation, you can use the more general {@link javax.inject.Scope}
 * annotation.
 *
 * <pre>{@code
 *    ㅤ@Scope
 *    ㅤ@Retention(RUNTIME)
 *   ㅤ@Target(METHOD)
 *     public @interface GcPlanetScope {
 *     }
 * }</pre>
 * <p>
 * Not Follow {@link javax.inject.Scope} documentation.
 */

@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.ANNOTATION_TYPE)
public @interface GcScopeAnnotation {
}
