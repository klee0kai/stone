package stone.annotations.component


import javax.inject.Scope

/**
 * A standard library scope that enumerates all cacheable objects using strong references.
 * Used for garbage collection and caching change methods.
 */
@stone.annotations.component.GcScopeAnnotation
@Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcStrongScope 
