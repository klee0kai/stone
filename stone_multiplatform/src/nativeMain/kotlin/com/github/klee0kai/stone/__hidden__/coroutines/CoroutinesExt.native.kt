package com.github.klee0kai.stone.__hidden__.coroutines

import kotlinx.coroutines.sync.Mutex

actual fun <T, R> T.syncIfAvailable(
    mutex: Mutex,
    block: T.() -> R
): R = block()

