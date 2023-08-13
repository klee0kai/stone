package com.github.klee0kai.stone.annotations.module;


import java.lang.annotation.*;

/**
 * Those objects that are created outside of DI.
 * Can be included in DI by using the `@BindInstance` annotation
 * <p>
 * Binding can be declared in a module.
 * To do this, we define the provider method in the module and the binding method in the module.
 * <pre>{@code
 *    ㅤ@Component
 *     public interface SpaceComponent {
 *
 *         SunSystemModule sunSystem();
 *
 *        ㅤ@BindInstance
 *         void bindSun(Sun sun);
 *
 *     }
 *
 *    ㅤ@Module
 *     public interface SunSystemModule {
 *
 *        ㅤ@BindInstance
 *         Sun sun();
 *
 *     }
 * }</pre>
 * <p>
 * The method in the module should not contain arguments, but only return an object.
 * A method in a component should only receive that object as an argument.
 * Further, this object can already be passed in the component directly or through injection.
 * <pre>{@code
 *    ㅤ@Component
 *     public interface SpaceComponent {
 *         // some code
 *
 *         // providing method
 *         Sun sun();
 *
 *     }
 * }</pre>
 * <p>
 * The second way to declare the use of binding is to declare everything in one method in the DI component.
 * <pre>{@code
 *    ㅤ@Component
 *     public interface SpaceComponent {
 *
 *        ㅤ@BindInstance
 *         Sun sun(Sun sun);
 *
 *     }
 * }</pre>
 * <p>
 * The method receives and returns the same type. If null is passed as an argument,
 * then the value of the binding does not change, but the object is simply provided.
 * <p>
 * Binding does not support nulling. Use different caching methods.
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface BindInstance {

    enum CacheType {
        Weak,
        Soft,
        Strong
    }

    /**
     * Object caching type
     * <ul>
     *  <li> Factory - creation of new objects without caching.</li>
     * <li> Weak - caching weak reference objects.
     * <li> Soft - caching objects with a soft link.
     * <li>Strong - caching objects with a strong link.</li>
     * </ul>
     * <p>
     */
    BindInstance.CacheType cache() default BindInstance.CacheType.Soft;

}
