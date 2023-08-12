package com.github.klee0kai.stone.annotations.component;


import javax.inject.Scope;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A standard library scope that lists all cached objects using soft references.
 * Used for garbage collection and caching change methods.
 */

@GcScopeAnnotation
@Scope
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface GcSoftScope {
}
