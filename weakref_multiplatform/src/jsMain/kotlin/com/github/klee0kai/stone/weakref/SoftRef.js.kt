package com.github.klee0kai.stone.weakref

actual class SoftRef<T : Any?> actual constructor(value: T) : Ref<T?>, AutoCloseable {

    private var weakRefDynamic: dynamic = null
    private var strongFallback: T? = null

    init {
        val hasWeakRef = js("typeof WeakRef !== 'undefined'") as Boolean
        if (hasWeakRef) {
            weakRefDynamic = js("new WeakRef")(value)
            strongFallback = null
        } else {
            weakRefDynamic = null
            strongFallback = value
        }
    }


    actual override fun get(): T? {
        return if (weakRefDynamic != null) {
            val derefResult = weakRefDynamic.deref?.invoke()
            derefResult as T?
        } else {
            strongFallback
        }
    }

    actual fun clear() {
        weakRefDynamic = null
        strongFallback = null
    }

    actual override fun close() {
        weakRefDynamic = null
        strongFallback = null
    }

}