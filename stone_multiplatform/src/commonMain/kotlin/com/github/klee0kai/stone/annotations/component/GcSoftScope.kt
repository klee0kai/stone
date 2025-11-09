package com.github.klee0kai.stone.annotations.component


import com.github.klee0kai.stone.weakref.Scope

/**
 * A standard library scope that lists all cached objects using soft references.
 * Used for garbage collection and caching change methods.
 */
@com.github.klee0kai.stone.annotations.component.GcScopeAnnotation
@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcSoftScope 
