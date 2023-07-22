package com.github.klee0kai.test.car.model

import java.util.*

class Bumper {
    @JvmField
    var uuid = UUID.randomUUID().toString()

    @JvmField
    var qualifier: String? = null

    init {
        createCount++
    }

    companion object {
        @JvmField
        var createCount = 0
    }
}
