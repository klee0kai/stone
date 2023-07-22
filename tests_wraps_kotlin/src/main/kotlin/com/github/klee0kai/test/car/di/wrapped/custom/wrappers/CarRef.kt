package com.github.klee0kai.test.car.di.wrapped.custom.wrappers

class CarRef<T>(v: T) {
    @JvmField
    var value: T? = null

    init {
        value = v
    }
}