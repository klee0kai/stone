package com.github.klee0kai.stone.weakref

actual interface Provider<T> {
    actual fun get(): T
}