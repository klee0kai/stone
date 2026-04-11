package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Ref

/**
 * A suspend-based provider that does **not** cache the value after provisioning.
 *
 * Each call to [get] (or [invoke]) re-executes the suspend lambda, making this
 * wrapper suitable for dependencies whose creation is asynchronous and where
 * a fresh instance is desired on every access.
 *
 * For a caching async variant, see [AsyncLazy].
 *
 * ### Usage in a component
 *
 * ```kotlin
 * @Component
 * interface CarWrappedCreateComponent {
 *     fun bumperAsyncPhantom(): AsyncProvider<Bumper>
 * }
 * ```
 *
 * ### Accessing the provided value
 *
 * ```kotlin
 * val DI = CarWrappedCreateComponentStoneComponent()
 * val bumper = DI.bumperAsyncPhantom()
 *
 * // suspend context required
 * runBlocking {
 *     val value = bumper.get()
 *     // or using invoke operator
 *     val value2 = bumper()
 * }
 * ```
 *
 * ### Construction from a [Ref]
 *
 * ```kotlin
 * val asyncProvider = AsyncProvider<Battery>(call = batteryRef)
 * runBlocking {
 *     val battery = asyncProvider.get()
 * }
 * ```
 *
 * @param T the type of object this provider supplies
 * @param provider the suspend lambda that produces an instance of [T]
 * @see AsyncLazy
 * @see com.github.klee0kai.stone.Provider
 * @see LazyProvider
 */
class AsyncProvider<T>(
    private val provider: suspend () -> T
) {

    /**
     * Creates an [AsyncProvider] backed by a [Ref].
     *
     * @param call the reference whose [Ref.get] is invoked on each access
     */
    constructor(call: Ref<T>) : this(provider = { call.get() })

    /**
     * Produces an instance of [T] by executing the underlying suspend lambda.
     * Does not cache the result — every call may return a new instance.
     */
    suspend fun get(): T = provider()

    /**
     * Operator shorthand for [get].
     *
     * ```kotlin
     * val value = asyncProvider()
     * ```
     */
    suspend operator fun invoke(): T = provider()

}