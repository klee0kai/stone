package com.github.klee0kai.test.boxed.di.inject

import com.github.klee0kai.stone.annotations.component.Component
import com.github.klee0kai.stone.annotations.component.ProtectInjected
import com.github.klee0kai.test.boxed.model.CarBoxedInject
import com.github.klee0kai.test.boxed.model.CarBoxedInjectLists
import com.github.klee0kai.test.boxed.model.CarBoxedInjectProvider

@Component
abstract class CarBoxedInjectComponent {
    protected abstract fun module(): CarBoxedInjectModule
    abstract fun inject(carInject: CarBoxedInject)
    abstract fun inject(carInject: CarBoxedInjectLists)
    abstract fun inject(carInject: CarBoxedInjectProvider)

    @ProtectInjected
    abstract fun protect(carInject: CarBoxedInject)

    @ProtectInjected
    abstract fun protect(carInject: CarBoxedInjectLists)

    @ProtectInjected
    abstract fun protect(carInject: CarBoxedInjectProvider)
}
