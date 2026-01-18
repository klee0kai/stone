package com.github.klee0kai.stone.weakref

import kotlin.js.js

actual object Memory {

    actual fun gc() {
        runCatching {
            js("gc()")
        }
    }

}