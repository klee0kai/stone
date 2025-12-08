package com.github.klee0kai.test.mowgli.body

import java.awt.Color
import java.util.*

class Blood(color: Color?) {

    var uuid: UUID = UUID.randomUUID()

    var color: Color? = null

    init {
        this.color = color
    }
}
