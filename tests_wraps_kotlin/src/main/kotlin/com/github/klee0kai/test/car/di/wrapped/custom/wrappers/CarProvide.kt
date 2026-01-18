package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

import com.github.klee0kai.stone.weakref.Ref

class CarProvide<T>(private val call: Ref<T>) {
    val value: T
        get() = call.get()
}
