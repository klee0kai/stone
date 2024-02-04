package com.github.klee0kai.wiki.cachecontrol

import com.github.klee0kai.stone.annotations.component.GcScopeAnnotation

@GcScopeAnnotation
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcMercuryScope

@GcScopeAnnotation
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class SunScope
