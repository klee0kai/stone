package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Ref
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

/**
 * An asynchronous lazy-evaluated provider that starts computation immediately
 * upon creation and caches the result.
 *
 * The suspend lambda is launched eagerly in [GlobalScope] on [Dispatchers.Default].
 * Calls to [get] (or [invoke]) suspend until the value is ready, then return
 * the cached result. This is ideal for expensive dependencies that should be
 * pre-computed in the background and shared across callers.
 *
 * For a non-caching async variant, see [AsyncProvider].
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface CarWrappedCreateComponent {
 *     fun carAsync(): AsyncLazy<Car?>?
 * }
 * ```
 *
 * ### Accessing the provided value
 *
 * ```kotlin
 * val DI = CarWrappedCreateComponentStoneComponent()
 *
 * runBlocking {
 *     val car1 = DI.carAsync()
 *     val car2 = DI.carAsync()
 *
 *     // both return the same cached instance
 *     assertEquals(car1!!.get()!!.uuid, car2!!.get()!!.uuid)
 * }
 * ```
 *
 * ### Dependency interface declarations
 *
 * ```kotlin
 * interface BerriesDependencies {
 *     fun currant(): AsyncLazy<Currant>
 *     fun raspberry(): AsyncLazy<Raspberry>
 *     fun strawberry(): AsyncLazy<Strawberry>
 * }
 *
 * interface BirdsDependencies {
 *     fun crow(): AsyncLazy<Crow>
 *     fun duck(): AsyncLazy<Duck>
 *     fun hen(): AsyncLazy<Hen>
 * }
 * ```
 *
 * ### Module-level provideWrapper
 *
 * ```kotlin
 * @Module(genProviderName = "TechFactoryProviders")
 * interface TechFactoryGenProvideModule {
 *     @Provide(cache = Provide.CacheType.Factory, provideWrapper = AsyncLazy::class)
 *     fun phoneOs(
 *         phoneOsType: PhoneOsType? = PhoneOsType.UbuntuTouch,
 *         version: PhoneOsVersion? = PhoneOsVersion(version = "def_version")
 *     ): OperationSystem?
 * }
 * ```
 *
 * @param T the type of object this provider supplies
 * @param provider the suspend lambda that produces an instance of [T],
 *   executed eagerly in [GlobalScope]
 * @see AsyncProvider
 * @see LazyProvider
 * @see com.github.klee0kai.stone.Provider
 */
class AsyncLazy<T>(
    private val provider: suspend () -> T
) {

    /**
     * Creates an [AsyncLazy] backed by a [Ref].
     *
     * @param call the reference whose [Ref.get] is invoked to produce the value
     */
    constructor(call: Ref<T>) : this(provider = { call.get() })

    @OptIn(DelicateCoroutinesApi::class)
    private val asyncValue = GlobalScope.async(Dispatchers.Default) {
        provider.invoke()
    }

    /**
     * Returns the cached instance of [T], suspending until the background
     * computation completes if it hasn't already.
     */
    suspend fun get(): T = asyncValue.await()

    /**
     * Operator shorthand for [get].
     *
     * ```kotlin
     * val value = asyncLazy()
     * ```
     */
    suspend operator fun invoke(): T = asyncValue.await()

}