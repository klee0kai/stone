@file:OptIn(NativeRuntimeApi::class)

package com.github.klee0kai.stone.weakref

import kotlin.native.runtime.NativeRuntimeApi

actual object Memory {

    actual fun gc() {
        runCatching {
            kotlin.native.runtime.GC.collect()
        }
    }

}