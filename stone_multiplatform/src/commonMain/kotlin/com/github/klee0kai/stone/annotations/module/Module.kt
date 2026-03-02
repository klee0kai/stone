package com.github.klee0kai.stone.annotations.module


/**
 * You can provide new dependencies and objects via modules.
 * Stone supports the announcement of new dependencies and providers.
 * A module can be a public class, an abstract class, or an interface.
 *
 *
 * Each object provision must use one of the 2 annotations.
 * `@Provide` - providing a new object
 * `@BindInstance` - providing an already known object in the application.
 * If you do not specify any of the specified annotations,
 * then the `@Provide` annotation is assumed by default.
 *
 *
 * So for example in the machine creation module
 * <pre>`ㅤ@Module
 * public abstract class CarModule {
 *
 * ㅤ@BindInstance
 * abstract Wheel wheel();
 *
 * ㅤ@Provide
 * Bumper bumper(){
 * return new Bumper();
 * }
 *
 * Window window(){
 * return new Window();
 * }
 *
 * }
`</pre> *
 *
 *
 * In our case, the wheel must be defined when initializing the DI component,
 * the bumper and the window have the same behavior - each time a new one is created.
 *
 *
 * Each element can be cached by specifying how the object is cached.
 * <pre>`ㅤ@Module
 * public abstract class CarModule {
 *
 * ㅤ@BindInstance(cache = BindInstance.CacheType.Weak)
 * abstract Wheel wheel();
 *
 * ㅤ@Provide(cache = Provide.CacheType.Weak)
 * Bumper bumper(){
 * return new Bumper();
 * }
 *
 * ㅤ@Provide(cache = Provide.CacheType.Soft)
 * Window window(){
 * return new Window();
 * }
 *
 * }
`</pre> *
 *
 *
 * And also, you can not explicitly specify the use of the constructor.
 * The constructor will be found by the library automatically when using parameters.
 *
 * <pre>`ㅤ@Module
 * public interface CarModule {
 *
 * ㅤ@BindInstance(cache = BindInstance.CacheType.Weak)
 * Wheel wheel();
 *
 * ㅤ@Provide(cache = Provide.CacheType.Weak)
 * Bumper bumper();
 *
 * ㅤ@Provide(cache = Provide.CacheType.Soft)
 * Window window();
 *
 * }
`</pre> *
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Module(
    val genProviderName: String = "",
)
