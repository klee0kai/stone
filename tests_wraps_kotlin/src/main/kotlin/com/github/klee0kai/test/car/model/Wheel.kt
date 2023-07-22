package com.github.klee0kai.test.car.model

import java.util.*

class Wheel {
    var uuid = UUID.randomUUID().toString()


    init {
        createCount++
    }

    companion object {
        var createCount = 0
    }
}
