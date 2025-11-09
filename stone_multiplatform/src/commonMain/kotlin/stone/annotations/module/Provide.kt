package stone.annotations.module

/**
 * Providing objects.
 * This annotation marks the methods in the module for providing objects.
 *
 *
 * The object can be created each time a new one, or cached.
 * To use a specific caching method, you must explicitly select a caching method.
 *
 *  *  Factory - creation of new objects without caching.
 *  *  Weak - caching weak reference objects.
 * The old object will be provided until the previous object is destroyed. None of the holders will let him go.
 *  *  Soft - caching objects with a soft link.
 * The old object  will be provided until the previous object is destroyed. None of the holders will let him go.
 *  * Strong - caching objects with a strong link.
 *
 *
 *
 * Dependencies for an object can be listed using function arguments.
 * <pre>`ㅤ@Module
 * interface CarModule {
 *
 * ㅤ@Provide(cache = Provide.CacheType.Weak)
 * Car car(Bumper bumper, Wheel wheel, Window window);
 *
 * }
`</pre> *
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Provide(
    /**
     * Object caching type
     */
    val cache: CacheType = CacheType.Factory
) {
    enum class CacheType {
        Factory,
        Weak,
        Soft,
        Strong
    }
}
