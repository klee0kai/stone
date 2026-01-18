package com.github.klee0kai.test.mowgli.earth

import java.util.*

class Cave : IMountain {
    var uuid: UUID = UUID.randomUUID()

    constructor()

    constructor(type: CaveType?, deep: Int?)

    enum class CaveType {
        Solutional, Glacier, Fracture
    }
}
