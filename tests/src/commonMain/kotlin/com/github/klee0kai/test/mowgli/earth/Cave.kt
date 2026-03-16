@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.earth

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Cave : IMountain {
    var uuid: String = Uuid.random().toString()

    constructor()

    constructor(type: CaveType?, deep: Int?)

    enum class CaveType {
        Solutional, Glacier, Fracture
    }
}
