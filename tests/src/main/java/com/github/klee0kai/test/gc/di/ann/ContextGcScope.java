package com.github.klee0kai.test.gc.di.ann;


import javax.inject.Scope;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Scope
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
public @interface ContextGcScope {
}
