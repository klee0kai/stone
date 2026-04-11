package com.github.klee0kai.stone

/**
 * Provides instances of [T]. For any type [T] that can be injected,
 * you can also inject `Provider<T>` to gain deferred access to the dependency.
 *
 * Unlike [com.github.klee0kai.stone.wrappers.LazyProvider], `Provider` does **not** cache the value —
 * each call to [get] may return a new instance depending on the module's caching strategy.
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface TechFactoryComponent {
 *     fun factory(): TechFactoryModule
 *     fun battery(): Provider<Battery>
 * }
 * ```
 *
 * ### Field and method injection
 *
 * ```kotlin
 * class GoodPhone {
 *     @Inject
 *     lateinit var battery: Provider<Battery>
 *
 *     fun create() {
 *         DI.inject(this)
 *     }
 * }
 * ```
 *
 * ### Accessing the provided value
 *
 * ```kotlin
 * val di = TechFactoryComponentStoneComponent()
 * val battery = di.batteryPhantomProvider()
 *
 * // each call to get() may produce a new instance (Factory caching)
 * val uuid1 = battery!!.get()!!.uuid
 * val uuid2 = battery.get()!!.uuid
 * ```
 *
 * ### Nested wrapper composition
 *
 * `Provider` can be composed with other wrappers for fine-grained control:
 *
 * ```kotlin
 * @Component
 * interface CarWrappedCreateComponent {
 *     fun whellProviderWeak(): Provider<WeakReference<Wheel?>?>?
 *     fun whellLazyProviderWeak(): LazyProvider<Provider<WeakReference<Wheel?>?>?>?
 * }
 * ```
 *
 * ### Module-level provideWrapper
 *
 * ```kotlin
 * @Module(genProviderName = "TechFactoryProviders")
 * interface TechFactoryGenProvideModule {
 *     @Named("null_args")
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = Provider::class)
 *     fun ram(): Ram?
 * }
 * ```
 *
 * @param T the type of object this provider supplies
 * @see com.github.klee0kai.stone.wrappers.LazyProvider
 * @see com.github.klee0kai.stone.wrappers.AsyncProvider
 */
fun interface Provider<T> {

    /**
     * Returns an instance of [T].
     *
     * The returned instance may be new or cached, depending on the
     * caching strategy configured in the corresponding [com.github.klee0kai.stone.Provide] annotation.
     */
    fun get(): T
}