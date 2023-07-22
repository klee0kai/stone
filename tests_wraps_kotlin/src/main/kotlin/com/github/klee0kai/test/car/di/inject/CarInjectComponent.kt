package com.github.klee0kai.test.car.di.inject

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarProviderWrapper
import com.github.klee0kai.test.car.model.CarInject
import com.github.klee0kai.test.car.model.CarInjectLists
import com.github.klee0kai.test.car.model.CarInjectProvider

@Component(wrapperProviders = [CarProviderWrapper::class])
abstract class CarInjectComponent {
    protected abstract fun module(): CarInjectModule?
    abstract fun inject(carInject: CarInject?)
    abstract fun inject(carInject: CarInjectLists?)
    abstract fun inject(carInject: CarInjectProvider?)

    @ProtectInjected
    abstract fun protect(carInject: CarInject?)

    @ProtectInjected
    abstract fun protect(carInject: CarInjectLists?)

    @ProtectInjected
    abstract fun protect(carInject: CarInjectProvider?)
}
