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

}