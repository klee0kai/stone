package com.github.klee0kai.stone.annotations.dependencies;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Component dependencies are provided through a class annotated with `@Dependencies`.
 * When resolving dependencies, the objects declared in this class will also be used.
 * <p>
 * The signature of a dependency class is the same as that of a module.
 * In this class, you just need to enumerate the provided objects and dependencies as interface methods.
 *
 * <pre>{@code
 *    @Dependencies
 *     public interface CarDependencies {
 *
 *         Wheel wheel();
 *
 *         Bumper bumper();
 *
 *         Window window();
 *
 *     }
 * }</pre>
 * Any factory, provider, or DI component can provide these dependencies by simply implementing this interface.
 * <pre>{@code
 *    @Comonent
 *     public abstract class AppComponent implements CommonDependencies {
 *         // some code
 *     }
 *
 *     @Dependencies
 *     public interface CommonDependencies{
 *         // some code
 *     }
 * }</pre>
 * <p>
 * In your component, you simply initialize these dependencies.
 * <pre>{@code
 *    @Comonent
 *     public abstract class FeatureComponent {
 *         public abstract CommonDependencies dependencies();
 *         @Init
 *         void initDependencies(CommonDependencies dependencies);
 *     }
 * }</pre>
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
public @interface Dependencies {
}
