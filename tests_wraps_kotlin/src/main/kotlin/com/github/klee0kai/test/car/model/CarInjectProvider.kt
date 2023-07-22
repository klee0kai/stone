package com.github.klee0kai.test.car.model

import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy
import javax.inject.Inject

class CarInjectProvider {
    @Inject
    var bumper: LazyProvide<Bumper>? = null

    @Inject
    var wheel: Ref<Wheel>? = null

    @Inject
    var window: Ref<CarLazy<Window>>? = null


    var bumperFromMethod: LazyProvide<Bumper>? = null
    var wheelFromMethod: Ref<Wheel>? = null
    var windowFromMethod: Ref<CarLazy<Window>>? = null

    @Inject
    fun init(bumper: LazyProvide<Bumper>?, wheel: Ref<Wheel>?, window: Ref<CarLazy<Window>>?) {
        bumperFromMethod = bumper
        wheelFromMethod = wheel
        windowFromMethod = window
    }
}
