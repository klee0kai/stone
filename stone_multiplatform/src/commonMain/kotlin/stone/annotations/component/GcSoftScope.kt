package stone.annotations.component


import javax.inject.Scope

/**
 * A standard library scope that lists all cached objects using soft references.
 * Used for garbage collection and caching change methods.
 */
@stone.annotations.component.GcScopeAnnotation
@javax.inject.Scope
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcSoftScope 
