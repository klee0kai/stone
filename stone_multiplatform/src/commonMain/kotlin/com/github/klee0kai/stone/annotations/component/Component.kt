package com.github.klee0kai.stone.annotations.component

import kotlin.reflect.KClass


/**
 * The main annotation for declaring a DI component.
 *
 * A component is the central entry point for providing and injecting dependencies.
 * It declares which modules create objects, how dependencies are resolved, and
 * how injection is performed. Can be applied to an **interface**, **abstract class**,
 * or a concrete **class**.
 *
 * Based on the annotated class, the Stone KSP processor generates a child class
 * named `<ClassName>StoneComponent` that implements all the DI wiring.
 *
 * ```kotlin
 * // Direct instantiation
 * val DI = SevenPlanetComponentStoneComponent()
 *
 * // Or via Stone factory
 * val DI = Stone.createComponent(SevenPlanetComponent::class)
 * ```
 *
 * ---
 *
 * ## Modules
 *
 * Modules are classes annotated with `@Module` that define how objects are created.
 * Declare a method returning the module type to make it available to the component:
 *
 * ```kotlin
 * @Component
 * interface SevenPlanetComponent {
 *     fun planets(): SevenPlanetModule
 * }
 * ```
 *
 * Modules can be used directly, and are also used internally to resolve dependencies
 * for provider and injection methods in the component.
 *
 * A module can be replaced at runtime via an [Init]-annotated initialization method:
 *
 * ```kotlin
 * @Component
 * interface AppComponent {
 *     fun feature(): FeatureModule
 *
 *     @Init
 *     fun initFeatureModule(featureModule: FeatureModule?)
 * }
 * ```
 *
 * Initialization can be performed at component creation or later.
 * Provided objects will be replaced gradually as old ones are cleared from memory.
 *
 * Multiple modules can be initialized in a single `@Init` method:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun united(): UnitedModule?
 *     fun identity(): IdentityModule?
 *
 *     @Init
 *     fun initUnitedModule(unitedModule: UnitedModule?)
 *
 *     @Init
 *     fun iniAllModules(unitedModule: UnitedModule?, identityModule: IdentityModule?)
 * }
 * ```
 *
 * ---
 *
 * ## Dependencies
 *
 * External dependencies from other components are declared the same way as modules,
 * but the dependency interface must be annotated with `@Dependencies`.
 * Dependencies **must be initialized before use** and are **not cached** —
 * they are already cached in their own component.
 *
 * ```kotlin
 * @Component
 * interface AppComponent {
 *     fun feature(): FeatureModule
 *     fun starsDependencies(): StarsDependencies
 *
 *     @Init
 *     fun initFeatureModule(featureModule: FeatureModule?)
 * }
 * ```
 *
 * ---
 *
 * ## Providing objects
 *
 * If a component has at least one module, it can directly provide any object
 * produced by its modules. Dependencies are resolved automatically:
 *
 * ```kotlin
 * @Component
 * interface CarWrappedCreateComponent {
 *     fun factory(): CarWrappedCreateModule?
 *
 *     fun wheel(): Wheel?
 *     fun car(): Car?
 *     fun window(): Window?
 * }
 * ```
 *
 * Return types can be wrapped with [com.github.klee0kai.stone.Provider],
 * [com.github.klee0kai.stone.wrappers.LazyProvider],
 * [com.github.klee0kai.stone.wrappers.AsyncLazy],
 * [com.github.klee0kai.stone.wrappers.AsyncProvider],
 * `WeakReference`, and other registered wrappers:
 *
 * ```kotlin
 * @Component
 * interface CarWrappedCreateComponent {
 *     fun factory(): CarWrappedCreateModule?
 *
 *     fun wheelProvide(): Provider<Wheel?>?
 *     fun wheelLazy(): LazyProvider<Wheel?>?
 *     fun wheelWeak(): WeakReference<Wheel?>?
 *     fun carAsync(): AsyncLazy<Car?>?
 *     fun bumperAsyncPhantom(): AsyncProvider<Bumper>
 *
 *     // nested wrappers are also supported
 *     fun whellProviderWeak(): Provider<WeakReference<Wheel?>?>?
 *     fun whellLazyProviderWeak(): LazyProvider<Provider<WeakReference<Wheel?>?>?>?
 * }
 * ```
 *
 * ---
 *
 * ## Identifiers
 *
 * Identifiers allow providing and caching **unique instances** of the same type.
 * Declare identifier types in the [identifiers] parameter; they must implement
 * `hashCode` and `equals` (Kotlin `data class`):
 *
 * ```kotlin
 * @Component(identifiers = [ScreenId::class, LoginId::class])
 * interface AppComponent {
 *     fun planetsModule(): SevenPlanetModule
 *     fun presentersModule(): PresentersModule
 *
 *     fun featurePresenter(loginId: LoginId, screenId: ScreenId): FeaturePresenter
 *
 *     fun inject(screen: FeatureScreen, loginId: LoginId, screenId: ScreenId)
 * }
 * ```
 *
 * Identifiers are also used when resolving dependencies — if a dependency uses an identifier,
 * it will be created with the matching key. If no identifier is passed, `null` is used.
 *
 * ---
 *
 * ## Bind instances
 *
 * Already existing objects can be provided as dependencies using `@BindInstance`.
 * Supports different cache types:
 *
 * ```kotlin
 * @Component
 * interface PlanetComponent {
 *     fun sunModule(): SunModule?
 *
 *     // bind and provide (with return type)
 *     @BindInstance
 *     fun planet(planet: IPlanet?): IPlanet?
 *
 *     // bind with weak caching
 *     @BindInstance(cache = BindInstance.CacheType.Weak)
 *     fun earth(earth: Earth?): Earth?
 *
 *     // bind only, no provide (void return)
 *     @BindInstance
 *     fun bindSun(sun: Sun?)
 *
 *     fun providePlanet(): IPlanet?
 * }
 * ```
 *
 * ---
 *
 * ## Injection
 *
 * Injection methods have no return type and accept a single argument — the target object.
 * When called, all fields and methods annotated with `@Inject` in the target are initialized:
 *
 * ```kotlin
 * @Component
 * interface TechFactoryComponent {
 *     fun factory(): TechFactoryModule
 *     fun battery(): Provider<Battery>
 *     fun ramMemory(): LazyProvider<Ram>
 *
 *     fun inject(goodPhone: GoodPhone)
 * }
 * ```
 *
 * Injection methods can also accept identifiers and a [com.github.klee0kai.stone.lifecycle.StoneLifeCycleOwner]:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun inject(horse: Horse?, stoneLifeCycleOwner: StoneLifeCycleOwner?)
 *     fun inject(horse: Horse?)
 *     fun inject(mowgli: Mowgli?)
 * }
 * ```
 *
 * ---
 *
 * ## Extension
 *
 * A component can extend another component with the [ExtendOf] annotation.
 * The child component inherits the parent's cached objects and modules,
 * while new objects are provided after old ones are cleared from memory:
 *
 * ```kotlin
 * @Component
 * interface AppProComponent : AppComponent {
 *     override fun feature(): ProFeatureModule
 *
 *     @ExtendOf
 *     fun extendComponent(parent: AppComponent)
 * }
 * ```
 *
 * ---
 *
 * ## Qualifiers
 *
 * Components support `@Named` and custom qualifier annotations to distinguish
 * between multiple instances of the same type:
 *
 * ```kotlin
 * @Component
 * interface CarQComponent {
 *     fun module1(): CarQPModule?
 *     fun module2(): CarQCModule?
 *
 *     @Named
 *     fun carNamedEmpty(): Car?
 *
 *     @Named("a")
 *     fun carNameA(): Car?
 *
 *     @MyQualifier
 *     fun carMyQualifier(): Car?
 *
 *     @MyQualifierMulti(type = MyQualifierMulti.Type.HARD, indx = 2, id = "a")
 *     fun carMyQualifierMultiA2Hard(): Car?
 *
 *     // collect all instances ignoring qualifiers
 *     @IgnoreQualifier
 *     fun allCars(): List<Car?>?
 * }
 * ```
 *
 * ---
 *
 * ## GC collect
 *
 * Explicit garbage collection for cached objects can be triggered via
 * methods annotated with [RunGc] and one or more scope annotations.
 * Only objects **not held** by the application will be cleared:
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent {
 *     abstract fun sunSystem(): GcSunSystemModule?
 *     abstract fun earth(): GcEarthModule?
 *
 *     @RunGc @GcAllScope
 *     abstract fun gcAll()
 *
 *     @RunGc @GcStrongScope
 *     abstract fun gcStrong()
 *
 *     @RunGc @GcSoftScope
 *     abstract fun gcSoft()
 *
 *     @RunGc @GcWeakScope
 *     abstract fun gcWeak()
 *
 *     @RunGc @GcSunScope
 *     abstract fun gcSun()
 *
 *     @RunGc @GcPlanetScope
 *     abstract fun gcPlanets()
 * }
 * ```
 *
 * ---
 *
 * ## Switch cache
 *
 * The caching strategy for provided objects can be changed at runtime
 * using [SwitchCache] combined with a GC scope. An optional `timeMillis`
 * parameter limits how long the new strategy stays active:
 *
 * ```kotlin
 * @Component
 * interface SwitchCacheComponent {
 *     fun earth(): GcEarthModule?
 *
 *     @GcAllScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Weak)
 *     fun allWeak()
 *
 *     @GcAllScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Strong, timeMillis = 100)
 *     fun allStrongFewMillis()
 *
 *     @GcStrongScope
 *     @SwitchCache(cache = SwitchCache.CacheType.Weak)
 *     fun strongToWeak()
 * }
 * ```
 *
 * ---
 *
 * ## Protect injected
 *
 * [ProtectInjected] temporarily protects injected objects from garbage collection
 * for the specified duration:
 *
 * ```kotlin
 * @Component
 * interface ForestComponent {
 *     fun inject(horse: Horse?)
 *     fun inject(mowgli: Mowgli?)
 *
 *     @ProtectInjected(timeMillis = 30)
 *     fun protectInjected(horse: Horse?)
 *
 *     @ProtectInjected(timeMillis = 30)
 *     fun protectInjected(horse: Mowgli?)
 * }
 * ```
 *
 * ---
 *
 * ## Custom wrapper helpers
 *
 * Additional wrapper types (beyond built-in `Provider`, `LazyProvider`, etc.)
 * can be registered with the [wrapperHelpers] parameter. See [com.github.klee0kai.stone.wrappers] for details:
 *
 * ```kotlin
 * @Component(wrapperHelpers = [CustomLazyWrapper::class])
 * interface TechFactoryComponent {
 *     fun factory(): TechFactoryModule
 *     fun battery(): Provider<Battery>
 *     fun ramMemory(): LazyProvider<Ram>
 *     fun inject(goodPhone: GoodPhone)
 * }
 * ```
 *
 * ---
 *
 * ## Abstract class form
 *
 * Components can be abstract classes, allowing concrete helper methods alongside
 * abstract DI declarations:
 *
 * ```kotlin
 * @Component
 * abstract class GcGodComponent : GcEarthComponent() {
 *     abstract fun sunSystem(): GcSunSystemModule?
 *
 *     @BindInstance
 *     abstract fun bind(sun: Sun?)
 *
 *     @RunGc @GcAllScope
 *     abstract fun gcAll()
 *
 *     // concrete helper combining multiple GC calls
 *     fun gcSunAndPlanets() {
 *         gcSun()
 *         gcPlanets()
 *     }
 * }
 * ```
 *
 * @see Init
 * @see ExtendOf
 * @see RunGc
 * @see SwitchCache
 * @see ProtectInjected
 * @see GcAllScope
 * @see com.github.klee0kai.stone.annotations.module.Module
 * @see com.github.klee0kai.stone.annotations.module.BindInstance
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Component(
    /**
     * Object identifier types that allow providing and caching **unique instances**
     * of the same object type, distinguished by identifier values.
     *
     *
     * Identifiers must implement `hashCode` and `equals` (Kotlin `data class`es are recommended).
     *
     *
     * ```kotlin
     * @Component(identifiers = [ScreenId::class, LoginId::class])
     * interface AppComponent {
     *     fun presentersModule(): PresentersModule
     *
     *     fun featurePresenter(loginId: LoginId, screenId: ScreenId): FeaturePresenter
     *
     *     fun inject(screen: FeatureScreen, loginId: LoginId, screenId: ScreenId)
     * }
     * ```
     *
     *
     * Identifiers are also used when resolving dependencies.
     * If a dependency uses an identifier, the dependency will be created with
     * the matching identifier key. To use different identifiers for a provided
     * object and its dependencies, use identifiers of different types.
     *
     *
     * If no identifier is specified when providing the object, `null` is used instead.
     */
    val identifiers: Array<KClass<*>> = [],

    /**
     * Custom wrapper helper classes that extend the set of supported return-type wrappers.
     *
     *
     * By default, Stone supports `Provider`, `LazyProvider`, `AsyncLazy`, `AsyncProvider`,
     * `WeakReference`, `WeakRef`, and `SoftRef` as return-type wrappers in component methods.
     * If you need a custom wrapper, create a helper object annotated with `@WrappersHelper`
     * that defines `transformTo*` methods accepting a `Provider<T>`, and register it here.
     *
     *
     * ```kotlin
     * @WrappersHelper
     * object CustomLazyWrapper {
     *     fun <T> transformToCarLay(
     *         origin: Provider<T>,
     *     ): CustomLazy<T> = CustomLazy { origin.get() }
     * }
     *
     * @Component(wrapperHelpers = [CustomLazyWrapper::class])
     * interface TechFactoryComponent {
     *     fun factory(): TechFactoryModule
     *     fun battery(): Provider<Battery>
     *     fun ramMemory(): LazyProvider<Ram>
     * }
     * ```
     *
     *
     * Multiple helpers can be specified:
     *
     *
     * ```kotlin
     * @Component(wrapperHelpers = [CarBoxedWrapper::class, CarRefWrapper::class])
     * interface CarCustomWrappersComponent {
     *     fun car(): Car?
     *     fun carRef(): CarRef<Car?>?
     *     fun carLazy(): CarLazy<Car?>?
     *     fun carProvide(): CarProvide<Car?>?
     * }
     * ```
     */
    val wrapperHelpers: Array<KClass<*>> = [],
)
