package com.github.klee0kai.stone.weakref

import java.lang.ref.WeakReference

actual class WeakRef<T> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    val weakRef: WeakReference<T> = WeakReference(value)

    actual override fun get(): T? = weakRef.get()

    actual fun clear() {
        weakRef.clear()
    }

    actual override fun close() {
        weakRef.clear()
    }

}