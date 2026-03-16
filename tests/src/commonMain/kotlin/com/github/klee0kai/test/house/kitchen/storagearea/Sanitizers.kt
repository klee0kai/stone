@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.kitchen.storagearea

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Sanitizers {

    var uuid: String = Uuid.random().toString()

    init {
        createCount++
    }

    companion object {
        var createCount: Int = 0
    }
}
