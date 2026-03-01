package com.github.klee0kai.stone.__hidden__.coroutines

import com.github.klee0kai.stone.mutex.ReentrantMutex
import kotlinx.coroutines.runBlocking

actual fun <T, R> T.syncIfAvailable(
    mutex: ReentrantMutex,
    block: T.() -> R,
): R = runBlocking {
    mutex.withLock {
        block()
    }
}
