package com.github.klee0kai.stone.annotations.module


/**
 * Marks a class or interface as a DI module that provides dependencies.
 *
 * A module is the primary place where object creation and binding logic is defined.
 * It can be a **class**, **abstract class**, or **interface**. Each method in a module
 * is either a provider (`@Provide`) or a binding (`@BindInstance`). If neither annotation
 * is specified, `@Provide(cache = CacheType.Factory)` is assumed by default.
 *
 * ---
 *
 * ## Basic module
 *
 * ```kotlin
 * @Module
 * abstract class SevenPlanetModule {
 *     @Provide(cache = Provide.CacheType.Soft)
 *     open fun earth(): Earth = Earth()
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     abstract fun mars(): Mars?
 * }
 * ```
 *
 * ---
 *
 * ## Interface module with automatic constructor resolution
 *
 * For abstract or interface methods without a body, Stone automatically finds and
 * invokes the appropriate constructor of the return type. Method parameters are
 * passed as constructor arguments:
 *
 * ```kotlin
 * @Module
 * interface RoomsModule {
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun kitchen(cookingArea: CookingArea?, sinkArea: SinkArea?): Kitchen?
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun bathRoom(storeArea: StoreArea?): BathRoom?
 * }
 * ```
 *
 * ---
 *
 * ## Caching strategies
 *
 * Each provider method can specify a caching strategy:
 *
 * ```kotlin
 * @Module
 * abstract class CarModule {
 *     @BindInstance(cache = BindInstance.CacheType.Weak)
 *     abstract fun wheel(wheel: Wheel? = null): Wheel?
 *
 *     @Provide(cache = Provide.CacheType.Weak)
 *     open fun bumper(): Bumper = Bumper()
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     open fun window(): Window = Window()
 * }
 * ```
 *
 * ---
 *
 * ## Bind instance
 *
 * Objects created outside of DI can be provided via `@BindInstance`:
 *
 * ```kotlin
 * @Module
 * interface SunSystemModule {
 *     @BindInstance(cache = BindInstance.CacheType.Weak)
 *     fun sun(sun: Sun? = null): Sun
 * }
 * ```
 *
 * ---
 *
 * ## Generated provider class
 *
 * When [genProviderName] is set, Stone generates an additional class that wraps
 * each provider method with the wrapper type specified in [Provide.provideWrapper]:
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
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = AsyncLazy::class)
 *     fun phoneOs(phoneOsType: PhoneOsType?): OperationSystem?
 * }
 * ```
 *
 * ---
 *
 * ## Module with qualifiers
 *
 * Qualifier annotations on methods distinguish multiple providers of the same type:
 *
 * ```kotlin
 * @Module
 * abstract class PresentersModule {
 *     @MyQualifier
 *     abstract fun provideFeaturePresenter(
 *         @ThreadQualifier(type = ThreadQualifier.ThreadType.Main) executor: ThreadPoolExecutor
 *     ): FeaturePresenter
 * }
 * ```
 *
 * ---
 *
 * ## Module with GC scopes
 *
 * GC scope annotations group cached objects for selective garbage collection:
 *
 * ```kotlin
 * @Module
 * interface PlanetsModule {
 *     @GcMercuryScope
 *     @BindInstance
 *     fun mercury(mercury: Mercury? = null): Mercury?
 *
 *     @Provide(cache = Provide.CacheType.Soft)
 *     fun venus(): Venus?
 * }
 * ```
 *
 * ---
 *
 * ## Module inheritance
 *
 * Modules can be subclassed to override creation logic:
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
 * ## Nuances
 *
 * - If a module is an **interface**, all methods are abstract and Stone resolves constructors automatically.
 * - If a module is an **abstract class**, you can mix abstract methods (auto-resolved) with
 *   concrete methods (custom creation logic).
 * - If a module is a **concrete class**, methods must be `open` to allow Stone to override them for caching.
 * - A module's methods are used both for direct access and for resolving dependencies
 *   in provider and injection methods of the component.
 * - The [genProviderName] parameter is optional and only needed when you want a separate
 *   generated provider class with wrapped return types.
 *
 * @see Provide
 * @see BindInstance
 * @see com.github.klee0kai.stone.annotations.component.Component
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Module(
    /**
     * Name for the generated provider class.
     *
     * When set, Stone generates an additional class with this name where each provider
     * method returns the object wrapped according to [Provide.provideWrapper].
     * Leave empty (default) if no separate provider class is needed.
     *
     * ```kotlin
     * @Module(genProviderName = "TechFactoryProviders")
     * interface TechFactoryGenProvideModule {
     *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = LazyProvider::class)
     *     fun battery(): Battery?
     * }
     * ```
     */
    val genProviderName: String = "",
)
