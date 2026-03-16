package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.weakref.Ref
import java.lang.ref.WeakReference

class CustomLazy<T>(call: Ref<T>) {

    private val weakRef: WeakReference<T> = WeakReference<T>(call.get())

    val value: T? get() = weakRef.get()

}