package com.github.klee0kai.test.boxed.model

import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import javax.inject.Inject

class CarBoxedInject {
    @Inject
    lateinit var bumper: CarBox<Bumper>

    @Inject
    lateinit var wheel: CarBox<Wheel>

    @Inject
    lateinit var window: CarBox<Window>


    var bumperFromMethod: CarBox<Bumper>? = null
    var wheelFromMethod: CarBox<Wheel>? = null
    var windowFromMethod: CarBox<Window>? = null

    @Inject
    fun init(bumper: CarBox<Bumper>?, wheel: CarBox<Wheel>?, window: CarBox<Window>?) {
        bumperFromMethod = bumper
        wheelFromMethod = wheel
        windowFromMethod = window
    }
}
