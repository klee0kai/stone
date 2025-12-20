package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

import com.github.klee0kai.stone.weakref.Provider

object CarRefWrapper {

    fun <T> transformToCarRef(
        origin: Provider<T>,
    ): CarRef<T> = CarRef(origin.get())

}