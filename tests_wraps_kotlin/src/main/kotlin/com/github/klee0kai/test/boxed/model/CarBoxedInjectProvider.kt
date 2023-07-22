package com.github.klee0kai.test.boxed.model

import com.github.klee0kai.stone.wrappers.LazyProvide
import com.github.klee0kai.stone.wrappers.Ref
import com.github.klee0kai.test.car.di.wrapped.custom.wrappers.CarLazy
import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import javax.inject.Inject

class CarBoxedInjectProvider {
    @Inject
    var bumper: LazyProvide<CarBox<Bumper>>? = null

    @Inject
    var wheel: Ref<CarBox<Wheel>>? = null

    @Inject
    var window: Ref<CarLazy<CarBox<Window>>>? = null


    var bumperFromMethod: LazyProvide<CarBox<Bumper>>? = null
    var wheelFromMethod: Ref<CarBox<Wheel>>? = null
    var windowFromMethod: Ref<CarLazy<CarBox<Window>>>? = null

    @Inject
    fun init(bumper: LazyProvide<CarBox<Bumper>>?, wheel: Ref<CarBox<Wheel>>?, window: Ref<CarLazy<CarBox<Window>>>?) {
        bumperFromMethod = bumper
        wheelFromMethod = wheel
        windowFromMethod = window
    }
}
