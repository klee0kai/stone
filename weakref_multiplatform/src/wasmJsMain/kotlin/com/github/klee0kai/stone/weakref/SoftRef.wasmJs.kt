package com.github.klee0kai.stone.weakref


actual class SoftRef<T> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    private var ref: T? = value

    actual override fun get(): T? = ref

    actual fun clear() {
        ref = null
    }

    actual override fun close() {
        ref = null
    }

}