package com.github.klee0kai.test.di.gcforest.scopes;

import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@GcScopeAnnotation
@Retention(RUNTIME)
@Target(METHOD)
public @interface GcPlanetScope {
}
