package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Ref

/**
 * Transparent provider of an object or dependency.
 * Does not cache the value after provisioning.
 */
class AsyncProvider<T>(
    private val provider: suspend () -> T
) {

    constructor(call: Ref<T>) : this(provider = { call.get() })

    suspend fun get(): T = provider()

    suspend operator fun invoke(): T = provider()

}