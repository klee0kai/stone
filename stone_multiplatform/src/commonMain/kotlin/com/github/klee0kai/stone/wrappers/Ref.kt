package com.github.klee0kai.stone.wrappers

import com.github.klee0kai.stone.weakref.Provider

fun interface Ref<T> : Provider<T> {

    override fun get(): T

}

