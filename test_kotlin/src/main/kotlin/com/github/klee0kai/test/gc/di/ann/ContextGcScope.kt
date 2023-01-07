package com.github.klee0kai.test.gc.di.ann

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class ContextGcScope 