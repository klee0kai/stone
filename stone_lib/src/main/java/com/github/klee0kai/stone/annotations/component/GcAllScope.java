package com.github.klee0kai.stone.annotations.component;


import javax.inject.Scope;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * run garbage collector for all elements
 */

@GcScopeAnnotation
@Scope
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface GcAllScope {
}
