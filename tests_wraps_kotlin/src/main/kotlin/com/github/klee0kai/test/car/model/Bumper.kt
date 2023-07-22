package com.github.klee0kai.test.car.model

import java.util.*

class Bumper {
    var uuid = UUID.randomUUID().toString()

    var qualifier: String? = null

    init {
        createCount++
    }

    companion object {
        var createCount = 0
    }
}
