package com.github.klee0kai.test.car.model

import java.util.*

class Window {

    var uuid = UUID.randomUUID().toString()

    init {
        createCount++
    }

    companion object {
        @JvmField
        var createCount = 0
    }
}
