package com.github.klee0kai.stone.__hidden__.types.holders

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.weakref.SoftRef
import com.github.klee0kai.stone.weakref.WeakRef

enum class StoneRefType {
    StrongObject,
    WeakObject,
    SoftObject,
    ListObject,
    ListWeakObject,
    ListSoftObject;

    val isList: Boolean
        get() = when (this) {
            StrongObject, WeakObject, SoftObject -> false
            ListObject, ListWeakObject, ListSoftObject -> true
        }

    fun forList(
    ): StoneRefType = when (this) {
        StrongObject -> ListObject
        WeakObject -> ListWeakObject
        SoftObject -> ListSoftObject
        else -> this
    }


    fun forSingle(
    ): StoneRefType = when (this) {
        ListObject -> StrongObject
        ListWeakObject -> WeakObject
        ListSoftObject -> SoftObject
        else -> this
    }

    fun <T> formatter(): ((T) -> Ref<T?>)? {
        when (this) {
            WeakObject, ListWeakObject -> return { WeakRef(it) }
            SoftObject, ListSoftObject -> return { SoftRef(it) }
            else -> return null
        }
    }
}
