package com.github.klee0kai.wiki.wrapping

import com.github.klee0kai.stone.annotations.wrappers.WrappersHelper
import com.github.klee0kai.stone.weakref.Provider

@WrappersHelper
object CustomLazyWrapper {

    fun <T> transformToCarLay(
        origin: Provider<T>,
    ): CustomLazy<T> = CustomLazy { origin.get() }

}