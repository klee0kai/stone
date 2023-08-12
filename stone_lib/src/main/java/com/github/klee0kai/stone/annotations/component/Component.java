package com.github.klee0kai.stone.annotations.component;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * The main component of providing dependencies.
 * Here we list the modules for creating objects.
 * Can be a public class, an abstract class, or an interface.
 * <p>
 * Based on this class, the library generates a child class `ClassName`StoneComponent.
 * Which can be used directly, or simplified notation.
 * <p>
 * `ClassName` DI = Stone.createComponent(`ClassName`.class);
 * <p>
 * <b>Modules</b>
 * <p>
 * In the DI component, you can declare the modules used.
 * To do this, you need to create a class with the @Module annotation, and provide it in the component.
 * <p>
 * <pre>{@code
 *    ㅤ@Component
 *     public abstract class AppComponent {
 *         public abstract RepositoriesModule repositories();
 *     }
 *
 *    ㅤ@Module
 *     public interface RepositoriesModule{
 *         // some code
 *     }
 * }</pre>
 * Modules can be used directly.
 * Also, declared modules in the component are used to resolve dependencies into injection methods,
 * as well as into provider methods in the component.
 * <p>
 * Providing a module can be replaced by calling an initialization method.
 * <pre>{@code
 *
 *    ㅤ@Component
 *     public abstract class AppComponent {
 *         public abstract RepositoriesModule repositories();
 *        ㅤ@Init
 *         void initRepositories(RepositoriesModule repositories);
 *     }
 *
 * }</pre>
 * <p>
 * Moreover, this initialization can be performed as when creating a component,
 * and when using this component directly.
 * In this case, the objects provided in the module will be replaced gradually as they are destroyed in memory.
 *
 * <p>
 * <b>Dependencies</b>
 * <p>
 * All component dependencies are declared in the same way as modules.
 * Dependency classes must use the `@Dependencies` annotation
 * <pre>{@code
 *    ㅤ@Component
 *     public abstract class FeatureComponent {
 *         public abstract CommonDependencies dependencies();
 *        ㅤ@Init
 *         void initDependencies(CommonDependencies dependencies);
 *     }
 *
 *    ㅤ@Dependencies
 *     public interface CommonDependencies{
 *         // some code
 *     }
 * }</pre>
 * Note that dependencies must be initialized before use.
 * <p>
 * Dependencies are not cached because
 * they are already cached in their component or in their factory, the provider.
 *
 * <p>
 * <b>Provide objects</b>
 * <p>
 * If a component contains at least one module, it can directly provide the dependencies
 * of that and its other modules directly.
 * <p>
 * In a component, you can declare a provider method of any object that is provided in one of its modules.
 * By providing this object, dependencies will be resolved automatically.
 * <pre>{@code
 *    ㅤ@Component
 *     public interface CarComponent {
 *
 *         CarModule module();
 *
 *         Car car();
 *     }
 *
 *    ㅤ@Module
 *     public interface CarModule{
 *         Wheel wheel();
 *         Window window();
 *         Bumper bumper();
 *
 *         Car car(Wheel wheel,Window window, Bumper bumper);
 *     }
 * }</pre>
 * You can also use additional identifiers to provide a unique object instance.
 * The result can be wrapped in declared wrappers, for example, lazy providing, and asynchronous providing.
 *
 * <p>
 * <b>Bind Instances</b>
 * <p>
 * The library allows you to use already known objects in the application as dependencies, as well as provisioning.
 * <p>
 * To do this, it is enough to declare the provision of an object with the `@BindInstance` annotation, if the declaration is performed directly in the component.
 * Or without this annotation, if it has already been used in the module.
 *
 * <p>
 * <b>Inject</b>
 * <p>
 * DI allows you to provide objects to classes.
 * To do this, it is enough to declare an injection method without a return type,
 * with only one argument - the injection class.
 * <pre>{@code
 *    ㅤ@Component
 *     public interface AppComponent {
 *         void inject(Activity activity);
 *     }
 * }</pre>
 * When this method is called,
 * all fields and methods with the `@Inject` annotation will be initialized or called.
 *
 * <p>
 * <b>Initialization</b>
 * <p>
 * The component allows you to initialize its dependencies and modules with the @Init annotation.
 *
 * <p>
 * <b>Extension</b>
 * <p>
 * A component can extend another component with the `ExtendOf` annotation.
 * When one component is extended by another, both begin to provide child component objects.
 * <p>
 * At the same time, object caching is temporarily preserved for the parent component.
 * New objects are provided after being cleared from memory,
 * or after explicitly using a child component.
 *
 * <pre>{@code
 *    ㅤ@Component
 *     public interface AppProComponent extends AppComponent {
 *
 *        ㅤ@ExtendOf
 *         void extOf(AppComponent parent);
 *
 *     }
 * }</pre>
 * <p>
 * <b>GC collect</b>
 * <p>
 * The library allows you to explicitly call garbage collection for cached objects.
 * To do this, you need to declare a method without arguments
 * and a return value with the annotation of one or more scopes.
 * <pre>{@code
 *    ㅤ@Component
 *     public interface AppProComponent extends AppComponent {
 *
 *        ㅤ@GcAllScope
 *         void gcAll();
 *
 *     }
 * }</pre>
 * Caching will be cleared only for those objects
 * that are not actually used in the application and are not held by anyone.
 * If you hold on to the provided and cached object when calling this method, it will not be destroyed.
 * <p>
 * <b>Switch cache</b>
 * <p>
 * DI also allows you to change the caching for provided objects.
 * To do this, you need to declare a method without arguments and a return value,
 * use one of the GC scopes, and additionally use the `@SwitchCache` annotation
 */
@Retention(value = RetentionPolicy.CLASS)
@Target(value = ElementType.TYPE)
public @interface Component {

    /**
     * You can use object identifiers.
     * Identifiers allow you to provide and cache unique instances of an object.
     * <p>
     * To use identifiers, you need to declare object types,
     * implementing the `hashCode` and `equals` methods (Kotlin data classes)
     * and list them when declaring the component.
     * <pre>{@code
     *    ㅤ@Component(identifiers = [PresenterTag::class])
     *     interface AppComponent {
     *         PresentersModule module();
     *         WelcomePresenter welcomePresenter(PresenterTag tag);
     *     }
     *
     *    ㅤ@Module
     *     interface PresentersModule {
     *         WelcomePresenter createWelcomePresenter(PresenterTag tag);
     *     }
     * }</pre>
     * <p>
     * Identifiers are also used when resolving dependencies.
     * For example, if a dependency uses an identifier,
     * then when providing an object with dependencies,
     * the dependency will be created by a unique identifier.
     * <p>
     * If you want to use different identifiers for a provided object and its dependencies,
     * use identifiers of different types.
     * <p>
     * If no identifier is specified when providing the object, it will be null instead.
     */
    Class[] identifiers() default {};


    /**
     * The library allows you to provide objects in wrappers.
     * Various wrappers allow, for example, to perform lazy or asynchronous providingF of objects,
     * or not to keep cached objects in DI.
     * Wrapping also allows you to render several different objects into one list.
     * <p>
     * The library contains a sufficient set of provider wrappers.
     * However, if that's not enough, you can implement your own wrapping implementation
     * class-based: `ProviderWrapper` and `Wrapper`.
     * It is enough to override these classes, and use the `@WrappersCreator` annotation
     * to list the wrappers implemented in this class.
     * <pre>{@code
     *    ㅤ@WrappersCreator(wrappers = {CarRef.class})
     *     public class CarWrapper implements Wrapper {
     *
     *        ㅤ@Override
     *         public <Wr, T> Wr wrap(Class<Wr> wrapperCl, T original) {
     *             if (wrapperCl.equals(CarRef.class)) {
     *                 return (Wr) new CarRef<>(original);
     *
     *             }
     *             return null;
     *         }
     *     }
     * }</pre>
     */
    Class[] wrapperProviders() default {};

}
