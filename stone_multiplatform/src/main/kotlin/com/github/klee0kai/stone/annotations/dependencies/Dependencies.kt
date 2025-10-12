package com.github.klee0kai.stone.annotations.dependencies


/**
 * Component dependencies are provided through a class annotated with `@Dependencies`.
 * When resolving dependencies, the objects declared in this class will also be used.
 *
 *
 * The signature of a dependency class is the same as that of a module.
 * In this class, you just need to enumerate the provided objects and dependencies as interface methods.
 *
 * <pre>`ㅤ@Dependencies
 * public interface CarDependencies {
 *
 * Wheel wheel();
 *
 * Bumper bumper();
 *
 * Window window();
 *
 * }
`</pre> *
 * Any factory, provider, or DI component can provide these dependencies by simply implementing this interface.
 * <pre>`ㅤ@Component
 * public abstract class AppComponent implements CommonDependencies {
 * // some code
 * }
 *
 * ㅤ@Dependencies
 * public interface CommonDependencies{
 * // some code
 * }
`</pre> *
 *
 *
 * In your component, you simply initialize these dependencies.
 * <pre>`ㅤ@Comonent
 * public abstract class FeatureComponent {
 * public abstract CommonDependencies dependencies();
 * ㅤ@Init
 * void initDependencies(CommonDependencies dependencies);
 * }
`</pre> *
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Dependencies 
