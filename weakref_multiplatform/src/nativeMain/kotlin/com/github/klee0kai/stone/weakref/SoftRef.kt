@file:OptIn(ExperimentalNativeApi::class)

package com.github.klee0kai.stone.weakref

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

actual class SoftRef<T : Any?> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    val weakRef: WeakReference<T & Any>? = value?.let { WeakReference(value) }

    actual override fun get(): T? = weakRef?.get()

    actual fun clear() {
        weakRef?.clear()
    }

    actual override fun close() {
        weakRef?.clear()
    }

}