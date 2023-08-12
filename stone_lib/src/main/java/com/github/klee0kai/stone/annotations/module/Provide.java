package com.github.klee0kai.stone.annotations.module;


import java.lang.annotation.*;

/**
 * Providing objects.
 * This annotation marks the methods in the module for providing objects.
 * <p>
 * The object can be created each time a new one, or cached.
 * To use a specific caching method, you must explicitly select a caching method.
 * <ul>
 *  <li> Factory - creation of new objects without caching.</li>
 * <li> Weak - caching weak reference objects.
 * The old object will be provided until the previous object is destroyed. None of the holders will let him go.</li>
 * <li> Soft - caching objects with a soft link.
 * The old object  will be provided until the previous object is destroyed. None of the holders will let him go.</li>
 * <li>Strong - caching objects with a strong link.</li>
 * </ul>
 * <p>
 * Dependencies for an object can be listed using function arguments.
 * <pre>{@code
 *   ㅤ@Module
 *     interface CarModule {
 *
 *        ㅤ@Provide(cache = Provide.CacheType.Weak)
 *         Car car(Bumper bumper, Wheel wheel, Window window);
 *
 *     }
 * }</pre>
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.METHOD)
@Inherited
public @interface Provide {

    enum CacheType {
        Factory,
        Weak,
        Soft,
        Strong
    }

    /**
     * Object caching type
     */
    CacheType cache() default CacheType.Factory;

}
