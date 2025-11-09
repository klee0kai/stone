package stone.annotations.module


/**
 * Those objects that are created outside of DI.
 * Can be included in DI by using the `@BindInstance` annotation
 *
 *
 * Binding can be declared in a module.
 * To do this, we define the provider method in the module and the binding method in the module.
 * <pre>`ㅤ@Component
 * public interface SpaceComponent {
 *
 * SunSystemModule sunSystem();
 *
 * ㅤ@BindInstance
 * void bindSun(Sun sun);
 *
 * }
 *
 * ㅤ@Module
 * public interface SunSystemModule {
 *
 * ㅤ@BindInstance
 * Sun sun();
 *
 * }
`</pre> *
 *
 *
 * The method in the module should not contain arguments, but only return an object.
 * A method in a component should only receive that object as an argument.
 * Further, this object can already be passed in the component directly or through injection.
 * <pre>`ㅤ@Component
 * public interface SpaceComponent {
 * // some code
 *
 * // providing method
 * Sun sun();
 *
 * }
`</pre> *
 *
 *
 * The second way to declare the use of binding is to declare everything in one method in the DI component.
 * <pre>`ㅤ@Component
 * public interface SpaceComponent {
 *
 * ㅤ@BindInstance
 * Sun sun(Sun sun);
 *
 * }
`</pre> *
 *
 *
 * The method receives and returns the same type. If null is passed as an argument,
 * then the value of the binding does not change, but the object is simply provided.
 *
 *
 * Binding does not support nulling. Use different caching methods.
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class BindInstance(
    /**
     * Object caching type
     *
     *  *  Factory - creation of new objects without caching.
     *  *  Weak - caching weak reference objects.
     *  *  Soft - caching objects with a soft link.
     *  * Strong - caching objects with a strong link.
     *
     *
     *
     */
    val cache: CacheType = CacheType.Soft
) {
    enum class CacheType {
        Weak,
        Soft,
        Strong
    }
}
