package com.github.klee0kai.stone.annotations.module;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * You can provide new dependencies and objects via modules.
 * Stone supports the announcement of new dependencies and providers.
 * A module can be a public class, an abstract class, or an interface.
 * <p>
 * Each object provision must use one of the 2 annotations.
 * `@Provide` - providing a new object
 * `@BindInstance` - providing an already known object in the application.
 * If you do not specify any of the specified annotations,
 * then the `@Provide` annotation is assumed by default.
 * <p>
 * So for example in the machine creation module
 * <pre>{@code
 *     @Module
 *     public abstract class CarModule {
 *
 *         @BindInstance
 *         abstract Wheel wheel();
 *
 *         @Provide
 *         Bumper bumper(){
 *             return new Bumper();
 *         }
 *
 *         Window window(){
 *             return new Window();
 *         }
 *
 *     }
 * }</pre>
 * <p>
 * In our case, the wheel must be defined when initializing the DI component,
 * the bumper and the window have the same behavior - each time a new one is created.
 * <p>
 * Each element can be cached by specifying how the object is cached.
 * <pre>{@code
 *    @Module
 *     public abstract class CarModule {
 *
 *         @BindInstance(cache = BindInstance.CacheType.Weak)
 *         abstract Wheel wheel();
 *
 *          @Provide(cache = Provide.CacheType.Weak)
 *         Bumper bumper(){
 *             return new Bumper();
 *         }
 *
 *         @Provide(cache = Provide.CacheType.Soft)
 *         Window window(){
 *             return new Window();
 *         }
 *
 *     }
 *  }</pre>
 * <p>
 * And also, you can not explicitly specify the use of the constructor.
 * The constructor will be found by the library automatically when using parameters.
 *
 * <pre>{@code
 *     @Module
 *     public interface CarModule {
 *
 *         @BindInstance(cache = BindInstance.CacheType.Weak)
 *         Wheel wheel();
 *
 *         @Provide(cache = Provide.CacheType.Weak)
 *         Bumper bumper();
 *
 *         @Provide(cache = Provide.CacheType.Soft)
 *         Window window();
 *
 *     }
 * }</pre>
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
public @interface Module {
}
