package com.github.klee0kai.stone.wrappers

import javax.inject.Provider

fun interface Ref<T> : Provider<T> {

    override fun get(): T

}

