package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Ref

/**
 * A lazy-evaluated provider that caches the result after the first access.
 *
 * The underlying [Ref] is invoked only on the first call to [get];
 * all subsequent calls return the cached value. This is useful when the
 * dependency is expensive to create and should be shared across callers.
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface TechFactoryComponent {
 *     fun factory(): TechFactoryModule
 *     fun ramMemory(): LazyProvider<Ram>
 * }
 * ```
 *
 * ### Usage in a component with other wrappers
 *
 * ```kotlin
 * @Component
 * interface CarWrappedCreateComponent {
 *     fun wheelLazy(): LazyProvider<Wheel?>?
 *     fun carLazy(): LazyProvider<Car?>?
 *     fun whellLazyProviderWeak(): LazyProvider<Provider<WeakReference<Wheel?>?>?>?
 * }
 * ```
 *
 * ### Field and method injection
 *
 * ```kotlin
 * class GoodPhone {
 *     @Inject
 *     lateinit var ram: LazyProvider<Ram>
 *
 *     fun create() {
 *         DI.inject(this)
 *     }
 * }
 *
 * class CarInjectProvider {
 *     @Inject
 *     var bumper: LazyProvider<Bumper>? = null
 *
 *     @Inject
 *     fun init(bumper: LazyProvider<Bumper>) {
 *         bumperFromMethod = bumper
 *     }
 * }
 * ```
 *
 * ### Accessing the provided value
 *
 * ```kotlin
 * val DI = TechFactoryComponentStoneComponent()
 * val battery = DI.batteryLazy()
 *
 * // both calls return the same cached instance
 * assertEquals(battery?.get()?.uuid, battery?.get()?.uuid)
 * ```
 *
 * ### Dependency interface declarations
 *
 * ```kotlin
 * interface SolarSystemDependencies {
 *     fun earth(): LazyProvider<Earth?>?
 *     fun mercury(): LazyProvider<Mercury?>?
 *     fun saturn(): LazyProvider<Saturn?>?
 * }
 * ```
 *
 * ### Module-level provideWrapper
 *
 * ```kotlin
 * @Module(genProviderName = "TechFactoryProviders")
 * interface TechFactoryGenProvideModule {
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = LazyProvider::class)
 *     fun battery(): Battery?
 * }
 * ```
 *
 * @param T the type of object this provider supplies
 * @param call the underlying [Ref] used to create the value on first access
 * @see com.github.klee0kai.stone.Provider
 * @see AsyncProvider
 * @see AsyncLazy
 */
class LazyProvider<T>(private val call: Ref<T>) : Ref<T?> {

    private var value: T? = null

    /**
     * Returns the cached instance of [T], creating it on the first call.
     *
     * The first invocation delegates to the underlying [Ref.get]; all
     * subsequent invocations return the same cached instance.
     */
    override fun get(): T {
        value?.let { return it }
        return call.get().also { value = it }
    }

}
