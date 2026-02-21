package com.github.klee0kai.stone.weakref

import js.WeakRef as JsWeakRef

actual class SoftRef<T : Any?> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    var weakRef = (value as? JsAny)?.let { JsWeakRef(value) }

    actual override fun get(): T? = weakRef?.deref() as T?

    actual fun clear() {
        weakRef = null
    }

    actual override fun close() {
        weakRef = null
    }

}