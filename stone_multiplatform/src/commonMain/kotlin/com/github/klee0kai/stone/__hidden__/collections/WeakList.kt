package com.github.klee0kai.stone.__hidden__.collections


import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.weakref.WeakRef

/**
 * Stone Private class
 */
class WeakList<T> {
    private val list = mutableListOf<Ref<T?>?>()

    val size: Int get() = list.size

    val isEmpty: Boolean get() = list.isEmpty()

    fun add(it: T): Boolean {
        clearNulls()
        return list.add(WeakRef(it))
    }

    fun add(idx: Int, it: T?) {
        clearNulls()
        list.add(idx, WeakRef(it))
    }

    fun remove(it: Any?): Boolean {
        return clearNulls(it)
    }

    fun clear() {
        list.clear()
    }

    fun clearNulls(
        ob: Any? = null,
    ): Boolean = list.removeAll { it?.get() == null || it.get() == ob }

    fun toList(): List<T?> {
        clearNulls()
        return list.mapNotNull { it?.get() }
    }

    fun get(idx: Int): T? = list[idx]?.get()

}
