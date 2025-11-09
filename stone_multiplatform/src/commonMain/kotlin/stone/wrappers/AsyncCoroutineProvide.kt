package stone.wrappers

import kotlinx.coroutines.*

class AsyncCoroutineProvide<T>(
    private val provider: suspend () -> T
) {

    constructor(call: stone.wrappers.Ref<T>) : this(provider = { call.get() })

    @OptIn(DelicateCoroutinesApi::class)
    private val asyncValue = GlobalScope.async(Dispatchers.Default) {
        provider.invoke()
    }

    suspend fun get(): T = asyncValue.await()

    suspend operator fun invoke(): T = asyncValue.await()

    fun syncGet() = runBlocking { provider.invoke() }

}