package com.github.klee0kai.test.car.model

import javax.inject.Inject

class CarInjectLists {
    @Inject
    var bumpers: List<Bumper>? = null

    @Inject
    var wheels: List<Wheel>? = null

    @Inject
    var windows: List<Window>? = null


    var bumpersMethodFrom: List<Bumper>? = null
    var wheelsMethodFrom: List<Wheel>? = null
    var windowsMethodFrom: List<Window>? = null

    @Inject
    fun init(bumpers: List<Bumper>?, wheels: List<Wheel>?, windows: List<Window>?) {
        bumpersMethodFrom = bumpers
        wheelsMethodFrom = wheels
        windowsMethodFrom = windows
    }
}
