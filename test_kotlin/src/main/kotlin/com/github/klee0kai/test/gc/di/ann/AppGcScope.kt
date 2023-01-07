package com.github.klee0kai.test.gc.di.ann

import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation

@GcScopeAnnotation
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class AppGcScope 