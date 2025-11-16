package com.github.klee0kai.stone.__hidden__.collections

import com.github.klee0kai.stone.weakref.Ref

class RefCollection<T> {

    private val refs = mutableListOf<Ref<T?>>()

    fun add(
        ref: Ref<T?>,
    ) {
        clearNulls()
        refs.add(ref)
    }

    val allRefs: List<Ref<T?>>
        get() {
            clearNulls()
            return refs
        }

    val all: List<T>
        get() {
            clearNulls()
            return refs.mapNotNull { it.get() }
        }

    fun clearNulls() {
        refs.removeAll { it.get() == null }
    }
}
