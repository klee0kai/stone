package com.github.klee0kai.test.car.model

import javax.inject.Inject

class CarInject {
    @Inject
    var bumper: Bumper? = null

    @Inject
    var wheel: Wheel? = null

    @Inject
    var window: Window? = null


    var bumperFromMethod: Bumper? = null
    var wheelFromMethod: Wheel? = null
    var windowFromMethod: Window? = null

    @Inject
    fun init(bumper: Bumper?, wheel: Wheel?, window: Window?) {
        bumperFromMethod = bumper
        wheelFromMethod = wheel
        windowFromMethod = window
    }
}
