package com.github.klee0kai.stone.annotations.component

/**
 * A scope that defines all exposed objects in DI.
 * Used for garbage collection and caching change methods.
 */
@com.github.klee0kai.stone.annotations.component.GcScopeAnnotation
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcAllScope 
