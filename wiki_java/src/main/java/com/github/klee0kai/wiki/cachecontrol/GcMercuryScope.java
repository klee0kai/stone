package com.github.klee0kai.wiki.cachecontrol;

import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@GcScopeAnnotation
@Retention(RUNTIME)
@Target(METHOD)
public @interface GcMercuryScope {
}
