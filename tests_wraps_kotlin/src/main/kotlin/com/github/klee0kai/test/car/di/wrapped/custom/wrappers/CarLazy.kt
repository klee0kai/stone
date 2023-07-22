package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

import com.github.klee0kai.stone.wrappers.Ref

class CarLazy<T>(private val call: Ref<T>) {
    var value: T? = null
        get() = if (field != null) field else call.get().also { field = it }
        private set
}
