package com.github.klee0kai.test.house.kitchen.storagearea

import java.util.*

class Sanitizers {
    @JvmField
    var uuid: UUID = UUID.randomUUID()

    init {
        createCount++
    }

    companion object {
        var createCount: Int = 0
    }
}
