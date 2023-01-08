package com.github.klee0kai.test.di.base_forest.scopes;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Retention(RUNTIME)
@Target(METHOD)
public @interface MountainGcScope {
}
