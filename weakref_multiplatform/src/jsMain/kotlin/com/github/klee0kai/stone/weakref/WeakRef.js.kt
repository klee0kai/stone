package com.github.klee0kai.stone.weakref

actual class WeakRef<T> actual constructor(value: T) {

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

    actual fun get(): T? {
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

}