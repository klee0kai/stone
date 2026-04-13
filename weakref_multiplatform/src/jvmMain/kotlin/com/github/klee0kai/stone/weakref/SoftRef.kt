package com.github.klee0kai.stone.weakref

import java.lang.ref.SoftReference

actual class SoftRef<T> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    val weakRef: SoftReference<T> = SoftReference(value)

    actual override fun get(): T? = weakRef.get()

    actual fun clear() {
        weakRef.clear()
    }

    actual override fun close() {
        weakRef.clear()
    }

    actual override fun hashCode(): Int {
        return get().hashCode()
    }

    actual override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other == null) return false
        if (this::class != other::class) return false
        return get() == (other as SoftRef<*>?)?.get()
    }

}