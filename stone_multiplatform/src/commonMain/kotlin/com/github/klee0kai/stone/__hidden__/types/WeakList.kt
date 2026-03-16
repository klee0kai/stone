package com.github.klee0kai.stone.__hidden__.types


import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.weakref.WeakRef

/**
 * Stone Private class
 */
class WeakList<T> {
    private val list: MutableList<Ref<T?>?> = mutableListOf()

    val size get() = list.size

    val isEmpty: Boolean get() = list.isEmpty()

    fun add(it: T?): Boolean {
        clearNulls()
        return list.add(WeakRef(it))
    }

    fun add(idx: Int, it: T?) {
        clearNulls()
        list.add(idx, WeakRef(it))
    }

    fun remove(it: T?): Boolean {
        return clearNulls(it)
    }

    fun clear() {
        list.clear()
    }

    fun clearNulls(
        ob: T? = null,
    ) = list.removeAll { item -> item?.get() == null || item.get() == ob }


    fun toList(): List<T> {
        clearNulls()
        return list.mapNotNull { item -> item?.get() }
    }

    fun get(idx: Int): T? = list[idx]?.get()

}
