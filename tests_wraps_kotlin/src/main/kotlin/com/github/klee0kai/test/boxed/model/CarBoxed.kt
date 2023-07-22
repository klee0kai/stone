package com.github.klee0kai.test.boxed.model

import com.github.klee0kai.test.car.model.Bumper
import com.github.klee0kai.test.car.model.Wheel
import com.github.klee0kai.test.car.model.Window
import java.util.*

class CarBoxed {
    var uuid = UUID.randomUUID().toString()
    var bumpers: List<CarBox<Bumper>>
    var wheels: List<CarBox<Wheel>>
    var windows: List<CarBox<Window>>

    constructor(
        bumper: CarBox<Bumper>,
        wheel: CarBox<Wheel>,
        window: CarBox<Window>
    ) {
        createCount++
        bumpers = listOf(bumper)
        wheels = listOf(wheel)
        windows = listOf(window)
    }

    constructor(bumpers: List<CarBox<Bumper>>, wheels: List<CarBox<Wheel>>, windows: List<CarBox<Window>>) {
        createCount++
        this.bumpers = bumpers
        this.wheels = wheels
        this.windows = windows
    }

    companion object {
        var createCount = 0
    }
}
