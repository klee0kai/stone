package com.github.klee0kai.test.car.model

import java.util.*

class Car {
    var uuid = UUID.randomUUID().toString()

    var bumpers: List<Bumper?>?
    var wheels: List<Wheel?>?
    var windows: List<Window?>?
    var qualifier: String? = null

    constructor(
        bumper: Bumper?,
        wheel: Wheel?,
        window: Window?
    ) {
        createCount++
        bumpers = listOf(bumper)
        wheels = listOf(wheel)
        windows = listOf(window)
    }

    constructor(bumpers: List<Bumper?>?, wheels: List<Wheel?>?, windows: List<Window?>?) {
        createCount++
        this.bumpers = bumpers
        this.wheels = wheels
        this.windows = windows
    }

    companion object {
        var createCount = 0
    }
}
