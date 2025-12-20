package com.github.klee0kai.test.boxed.di.inject

import com.github.klee0kai.stone.annotations.wrappers.WrappersHelper
import com.github.klee0kai.stone.weakref.Provider
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarProvide

@WrappersHelper
object CarBoxedWrapper {

    fun <T> transformToCarLay(
        origin: Provider<T>,
    ): CarLazy<T> = CarLazy { origin.get() }

    fun <T> transformToCarProvide(
        origin: Provider<T>,
    ): CarProvide<T> = CarProvide { origin.get() }

}