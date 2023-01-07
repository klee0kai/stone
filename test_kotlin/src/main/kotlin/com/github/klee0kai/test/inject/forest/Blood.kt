package com.github.klee0kai.test.inject.forest

import java.awt.Color
import java.util.*

class Blood(color: Color?) {
    var uuid = UUID.randomUUID()
    var color: Color? = null

    init {
        this.color = color
    }
}