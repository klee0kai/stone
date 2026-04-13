package com.github.klee0kai.stone.weakref

import js.WeakRef as JsWeakRef

actual class WeakRef<T : Any?> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    var weakRef: JsWeakRef<T>? = JsWeakRef(value)

    actual override fun get(): T? = weakRef?.deref()

    actual fun clear() {
        weakRef = null
    }

    actual override fun close() {
        weakRef = null
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