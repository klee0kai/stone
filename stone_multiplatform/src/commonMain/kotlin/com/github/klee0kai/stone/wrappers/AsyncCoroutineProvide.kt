package com.github.klee0kai.stone.wrappers

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class AsyncCoroutineProvide<T>(
    private val provider: suspend () -> T
) {

    constructor(call: Ref<T>) : this(provider = { call.get() })

    @OptIn(DelicateCoroutinesApi::class)
    private val asyncValue = GlobalScope.async(Dispatchers.Default) {
        provider.invoke()
    }

    suspend fun get(): T = asyncValue.await()

    suspend operator fun invoke(): T = asyncValue.await()

}