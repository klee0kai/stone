package com.github.klee0kai.stone.annotations.module

import kotlin.reflect.KClass

/**
 * Marks a method in a [Module] as a dependency provider.
 *
 * Methods annotated with `@Provide` define how objects are created and cached
 * within a module. The annotation is **optional** — any unannotated method in a module
 * is treated as `@Provide(cache = CacheType.Factory)` by default.
 *
 * ---
 *
 * ## Cache types
 *
 * The [cache] parameter controls how provided objects are stored between calls:
 *
 * - [CacheType.Factory] — a **new instance** is created on every call (default).
 * - [CacheType.Weak] — the instance is cached via a **weak reference**.
 *   The same object is returned as long as someone still holds a strong reference to it.
 * - [CacheType.Soft] — the instance is cached via a **soft reference**.
 *   The same object is returned until the JVM reclaims it under memory pressure.
 * - [CacheType.Strong] — the instance is cached with a **strong reference**
 *   and is never garbage-collected automatically.
 *
 * ```kotlin
 * @Module
 * abstract class GcEarthModule {
 *     @Provide(cache = Provide.CacheType.Strong)
 *     abstract fun mountainStrong(): Mountain?
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     abstract fun mountainSoft(): Mountain?
 *
 *     @Provide(cache = Provide.CacheType.Weak)
 *     abstract fun mountainWeak(): Mountain?
 *
 *     @Provide(cache = Provide.CacheType.Factory)
 *     open fun mountainFactory(): Mountain? = Mountain()
 * }
 * ```
 *
 * ---
 *
 * ## Abstract methods and automatic constructor resolution
 *
 * If the method has no body (abstract or interface method), Stone automatically
 * finds and invokes the appropriate constructor of the return type.
 * Method parameters are passed as constructor arguments:
 *
 * ```kotlin
 * @Module
 * interface RoomsModule {
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun kitchen(cookingArea: CookingArea?, sinkArea: SinkArea?, storeArea: StoreArea?): Kichen?
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun bathRoom(storeArea: StoreArea?): BathRoom?
 * }
 * ```
 *
 * A parameterless abstract method resolves the default (no-arg) constructor:
 *
 * ```kotlin
 * @Module
 * interface TechFactoryModule {
 *     @Provide(cache = Provide.CacheType.Factory)
 *     fun battery(): Battery
 *
 *     @Provide(cache = Provide.CacheType.Factory)
 *     fun ram(): Ram
 * }
 * ```
 *
 * ---
 *
 * ## Methods with a body
 *
 * Concrete (non-abstract) methods provide full control over object creation.
 * The method body is used as-is, and the cache type still applies:
 *
 * ```kotlin
 * @Module
 * open class SevenPlanetModule {
 *     @Provide(cache = Provide.CacheType.Soft)
 *     open fun earth(): Earth {
 *         return Earth()
 *     }
 * }
 * ```
 *
 * ---
 *
 * ## Dependencies via method parameters
 *
 * Method parameters declare dependencies that are resolved automatically
 * from the same component. Parameters support nullable types and default values:
 *
 * ```kotlin
 * @Module
 * interface HouseModule {
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun house(kichen: Kichen?, bathRoom: BathRoom?, bedRoom: BedRoom?, garage: Garage?): House?
 * }
 * ```
 *
 * Parameters can also be wrapper types (`Provider`, `WeakReference`, `List`, etc.)
 * — Stone resolves and wraps dependencies accordingly:
 *
 * ```kotlin
 * @Module
 * open class CarQCModule {
 *     @Provide(cache = Provide.CacheType.Factory)
 *     open fun carProvider(
 *         @BumperQualifier bumper: Provider<Bumper?>,
 *         wheel: Provider<Wheel?>,
 *         window: Provider<Window?>
 *     ): Car {
 *         return Car(bumper.get(), wheel.get(), window.get())
 *     }
 * }
 * ```
 *
 * Default parameter values are supported:
 *
 * ```kotlin
 * @Provide(cache = Provide.CacheType.Factory, provideWrapper = AsyncLazy::class)
 * fun phoneOs(
 *     phoneOsType: PhoneOsType? = PhoneOsType.UbuntuTouch,
 *     version: PhoneOsVersion? = PhoneOsVersion(version = "def_version")
 * ): OperationSystem?
 * ```
 *
 * ---
 *
 * ## Providing collections
 *
 * A method can return `List` or `Collection` to provide multiple objects at once:
 *
 * ```kotlin
 * @Module
 * abstract class CarMultiModule {
 *     @Provide(cache = Provide.CacheType.Factory)
 *     open fun fourWheels(): List<Wheel> {
 *         return listOf(Wheel(), Wheel(), Wheel(), Wheel())
 *     }
 *
 *     @Provide(cache = Provide.CacheType.Factory)
 *     open fun bumpers(): Collection<Bumper> {
 *         return listOf(Bumper(), Bumper())
 *     }
 * }
 * ```
 *
 * ---
 *
 * ## Generic return types
 *
 * Type parameters are preserved during resolution:
 *
 * ```kotlin
 * @Module
 * abstract class WireModule {
 *     @Provide(cache = Provide.CacheType.Soft)
 *     abstract fun usb_miniusb(): Wire<Usb?, MiniUsb?>?
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     open fun usb_hdmi(): Wire<Usb?, Hdmi?>? = Wire()
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     open fun simple(): Wire<*, *>? = Wire<Usb?, Usb?>()
 * }
 * ```
 *
 * ---
 *
 * ## Wrapper packaging with [provideWrapper]
 *
 * The [provideWrapper] parameter wraps the created object into the specified wrapper
 * type in the generated provider class (used with [Module.genProviderName]).
 * Supported wrappers: [com.github.klee0kai.stone.Provider], [com.github.klee0kai.stone.wrappers.LazyProvider],
 * [com.github.klee0kai.stone.wrappers.AsyncLazy], [com.github.klee0kai.stone.weakref.Ref],
 * [com.github.klee0kai.stone.weakref.WeakRef]:
 *
 * ```kotlin
 * @Module(genProviderName = "TechFactoryProviders")
 * interface TechFactoryGenProvideModule {
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = LazyProvider::class)
 *     fun battery(): Battery?
 *
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = Provider::class)
 *     fun ram(): Ram?
 *
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = Ref::class)
 *     fun ram(ramSize: RamSize? = null): Ram?
 *
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = WeakRef::class)
 *     fun phoneOsNamed(osType: PhoneOsType?): OperationSystem?
 *
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = AsyncLazy::class)
 *     fun phoneOs(phoneOsType: PhoneOsType?, version: PhoneOsVersion?): OperationSystem?
 * }
 * ```
 *
 * ---
 *
 * ## Qualifiers
 *
 * Qualifier annotations (`@Named`, custom qualifiers) on a `@Provide` method
 * distinguish multiple providers of the same type. Qualifiers on parameters
 * select which specific dependency to inject. Use `@IgnoreQualifier` on a parameter
 * to collect **all** instances regardless of their qualifier:
 *
 * ```kotlin
 * @Module
 * open class CarQCModule {
 *     @Named("redCar")
 *     @Provide(cache = Provide.CacheType.Factory)
 *     abstract fun redCar(bumper: Bumper?, wheel: Wheel?, window: Window?): Car?
 *
 *     @Named("blueCar")
 *     @Provide(cache = Provide.CacheType.Factory)
 *     abstract fun blueCar(
 *         bumpers: List<Bumper>,
 *         wheels: List<Wheel>,
 *         @IgnoreQualifier windows: List<Window>,
 *     ): Car?
 *
 *     @MyQualifierMulti(id = "a", indx = 2, type = MyQualifierMulti.Type.HARD)
 *     @Provide(cache = Provide.CacheType.Factory)
 *     open fun carQualifierMultiA2Hard(
 *         @BumperQualifier(type = BumperQualifier.BumperType.Simple) bumper: List<Bumper>,
 *         @WheelCount(count = 4) wheel: List<Wheel>,
 *         @IgnoreQualifier window: List<Window>
 *     ): Car { ... }
 * }
 * ```
 *
 * ---
 *
 * ## GC scope annotations
 *
 * GC scope annotations (e.g. `@GcMountainScope`) can be combined with `@Provide`
 * to group cached objects for selective garbage collection via `@RunGc`
 * or cache switching via `@SwitchCache` in the component:
 *
 * ```kotlin
 * @Module
 * abstract class GcEarthModule {
 *     @GcMountainScope
 *     @Provide(cache = Provide.CacheType.Strong)
 *     abstract fun mountainStrong(): Mountain?
 *
 *     @GcRiverScope
 *     @Provide(cache = Provide.CacheType.Soft)
 *     abstract fun riverSoft(): River?
 * }
 * ```
 *
 * Multiple GC scopes can be stacked on a single method:
 *
 * ```kotlin
 * @GcBumperScope
 * @GcBumperRedScope
 * @Provide(cache = Provide.CacheType.Strong)
 * open fun bumperStrong(): List<Bumper> { ... }
 * ```
 *
 * ---
 *
 * ## Module inheritance
 *
 * `@Provide` methods can be overridden in subclass modules to replace
 * creation logic while keeping the same cache type or changing it:
 *
 * ```kotlin
 * @Module
 * abstract class UnitedBlueModule : UnitedModule() {
 *     @Provide(cache = Provide.CacheType.Strong)
 *     override fun blood(): Blood? = Blood(1)
 * }
 * ```
 *
 * ---
 *
 * ## Implicit `@Provide`
 *
 * If neither `@Provide` nor `@BindInstance` is specified on a module method,
 * `@Provide(cache = CacheType.Factory)` is assumed:
 *
 * ```kotlin
 * @Module
 * abstract class SevenPlanetModule {
 *     // equivalent to @Provide(cache = CacheType.Factory)
 *     open fun earth(): Earth = Earth()
 *
 *     // equivalent to @Provide(cache = CacheType.Factory)
 *     abstract fun sun(): Sun
 * }
 * ```
 *
 * @see Module
 * @see BindInstance
 * @see com.github.klee0kai.stone.annotations.component.Component
 * @see com.github.klee0kai.stone.Provider
 * @see com.github.klee0kai.stone.wrappers.LazyProvider
 * @see com.github.klee0kai.stone.wrappers.AsyncLazy
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Provide(
    /**
     * Object caching strategy.
     *
     *
     * Determines how the provided object is stored between calls.
     * Defaults to [CacheType.Factory] (no caching — new instance every time).
     *
     *
     * ```kotlin
     * @Provide(cache = Provide.CacheType.Weak)
     * fun battery(): Battery?
     *
     * @Provide(cache = Provide.CacheType.Strong)
     * fun sun(): Sun? = Sun()
     * ```
     */
    val cache: CacheType = CacheType.Factory,

    /**
     * Wrapper type to package the provided dependency in the generated provider class.
     *
     *
     * When set, the generated provider (see [Module.genProviderName]) wraps
     * the created object into the specified type instead of returning it directly.
     * Supported wrapper types: [com.github.klee0kai.stone.Provider],
     * [com.github.klee0kai.stone.wrappers.LazyProvider],
     * [com.github.klee0kai.stone.wrappers.AsyncLazy],
     * [com.github.klee0kai.stone.weakref.Ref],
     * [com.github.klee0kai.stone.weakref.WeakRef].
     *
     *
     * Defaults to [Nothing] (no wrapping).
     *
     *
     * ```kotlin
     * @Provide(cache = Provide.CacheType.Factory, provideWrapper = LazyProvider::class)
     * fun battery(): Battery?
     *
     * @Provide(cache = Provide.CacheType.Factory, provideWrapper = AsyncLazy::class)
     * fun phoneOs(phoneOsType: PhoneOsType?): OperationSystem?
     * ```
     */
    val provideWrapper: KClass<*> = Nothing::class,
) {

    /**
     * Caching strategies for provided objects.
     */
    enum class CacheType {
        /** A new instance is created on every call. No caching. */
        Factory,

        /** The instance is cached via a weak reference. Reclaimed when no strong references remain. */
        Weak,

        /** The instance is cached via a soft reference. Reclaimed under memory pressure. */
        Soft,

        /** The instance is cached via a strong reference. Never reclaimed automatically. */
        Strong,
    }
}
