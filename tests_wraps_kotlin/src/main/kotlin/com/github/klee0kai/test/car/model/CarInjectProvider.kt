package com.github.klee0kai.test.car.model

import com.github.klee0kai.stone.weakref.Ref
import com.github.klee0kai.stone.wrappers.LazyProvider
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy
import javax.inject.Inject

class CarInjectProvider {
    @Inject
    var bumper: LazyProvider<Bumper>? = null

    @Inject
    var wheel: Ref<Wheel>? = null

    @Inject
    var window: Ref<CarLazy<Window>>? = null


    var bumperFromMethod: LazyProvider<Bumper>? = null
    var wheelFromMethod: Ref<Wheel>? = null
    var windowFromMethod: Ref<CarLazy<Window>>? = null

    @Inject
    fun init(bumper: LazyProvider<Bumper>, wheel: Ref<Wheel>, window: Ref<CarLazy<Window>>) {
        bumperFromMethod = bumper
        wheelFromMethod = wheel
        windowFromMethod = window
    }
}
