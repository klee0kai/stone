package com.github.klee0kai.stone.weakref

import kotlin.reflect.KProperty

class WeakRefDelegate<T : Any>(value: T) {

    private var weakRef = WeakRef(value)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return weakRef.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        if (value != null) {
            weakRef = WeakRef(value)
        }
    }

    fun clear() {
        weakRef.clear()
    }
}