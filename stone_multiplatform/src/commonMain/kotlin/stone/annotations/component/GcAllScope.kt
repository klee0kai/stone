package stone.annotations.component

/**
 * A scope that defines all exposed objects in DI.
 * Used for garbage collection and caching change methods.
 */
@GcScopeAnnotation
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class GcAllScope 
