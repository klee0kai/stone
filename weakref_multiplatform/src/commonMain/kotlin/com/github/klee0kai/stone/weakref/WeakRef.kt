package com.github.klee0kai.stone.weakref

expect class WeakRef<T : Any?> constructor(value: T) : Ref<T?>, AutoCloseable {

    override fun get(): T?

    fun clear()

    override fun close()

}