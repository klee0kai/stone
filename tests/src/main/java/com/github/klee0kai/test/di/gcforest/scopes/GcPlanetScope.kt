package com.github.klee0kai.test.di.gcforest.scopes

import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation

@GcScopeAnnotation
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcPlanetScope 
