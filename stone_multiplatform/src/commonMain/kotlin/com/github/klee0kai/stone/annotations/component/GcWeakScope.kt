package com.github.klee0kai.stone.annotations.component


import com.github.klee0kai.stone.weakref.Scope

/**
 * A standard library scope that enumerates all cacheable objects using weak references.
 * Used for garbage collection and caching change methods.
 */
@GcScopeAnnotation
@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcWeakScope 
