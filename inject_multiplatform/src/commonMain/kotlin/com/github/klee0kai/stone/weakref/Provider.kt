package com.github.klee0kai.stone.weakref

/**
 * Provides instances of [T]. Typically implemented by an injector. For
 * any type [T] that can be injected, you can also inject
 * `Provider<T>`.
 */
fun interface Provider<T> {
    fun get(): T
}