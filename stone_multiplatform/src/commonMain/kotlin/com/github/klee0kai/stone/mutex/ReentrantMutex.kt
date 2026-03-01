@file:OptIn(ExperimentalTime::class)

package com.github.klee0kai.stone.mutex

import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.sync.Mutex
import kotlin.time.ExperimentalTime

class ReentrantMutex {

    private val mutex = Mutex()
    private val owner = atomic<CoroutineId?>(null)
    private var count = 0

    suspend fun lock() {
        val current = coroutineId()
        if (owner.value == current) {
            count++
            return
        }
        mutex.lock()
        owner.value = current
        count = 1
    }

    suspend fun unlock() {
        val current = coroutineId()
        require(owner.value == current) { "Unlock from non-owner!" }
        count--
        if (count == 0) {
            owner.value = null
            mutex.unlock()
        }
    }

    suspend fun <T> withLock(action: suspend () -> T): T {
        lock()
        return try {
            action()
        } finally {
            unlock()
        }
    }
}

internal typealias CoroutineId = Long

internal suspend fun coroutineId(
): CoroutineId = currentCoroutineContext()[Job]?.hashCode()?.toLong() ?: error("No Job in context")
