package com.github.klee0kai.stone.wrappers

import javax.inject.Provider

interface Ref<T> : Provider<T> {

    override fun get(): T

}
