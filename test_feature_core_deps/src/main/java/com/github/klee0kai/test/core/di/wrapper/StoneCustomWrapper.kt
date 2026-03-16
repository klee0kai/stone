package com.github.klee0kai.test.core.di.wrapper

import com.github.klee0kai.stone.annotations.wrappers.WrappersHelper
import com.github.klee0kai.stone.Provider

@WrappersHelper
object StoneCustomWrapper {

    fun <T> transformToCustomWrapper(
        origin: Provider<T>,
    ): CustomStoneProvide<T> = CustomStoneProvide { origin.get() }

    fun <T> transformFromCustomWrapper(
        origin: CustomStoneProvide<T>,
    ): T = origin.get()

}