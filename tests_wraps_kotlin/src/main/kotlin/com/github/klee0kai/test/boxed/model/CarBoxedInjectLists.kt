package com.github.klee0kai.test.boxed.model

import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import javax.inject.Inject

class CarBoxedInjectLists {
    @Inject
    var bumpers: List<CarBox<Bumper>>? = null

    @Inject
    var wheels: List<CarBox<Wheel>>? = null

    @Inject
    var windows: List<CarBox<Window>>? = null


    var bumpersMethodFrom: List<CarBox<Bumper>>? = null
    var wheelsMethodFrom: List<CarBox<Wheel>>? = null
    var windowsMethodFrom: List<CarBox<Window>>? = null


    @Inject
    fun init(bumpers: List<CarBox<Bumper>>?, wheels: List<CarBox<Wheel>>?, windows: List<CarBox<Window>>?) {
        bumpersMethodFrom = bumpers
        wheelsMethodFrom = wheels
        windowsMethodFrom = windows
    }
}
