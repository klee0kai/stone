package com.github.klee0kai.stone.__hidden__.coroutines

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

actual fun <T, R> T.syncIfAvailable(
    mutex: Mutex,
    block: T.() -> R,
): R = runBlocking {
    mutex.withLock {
        block()
    }
}
