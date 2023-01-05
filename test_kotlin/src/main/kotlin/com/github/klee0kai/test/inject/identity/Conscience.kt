package com.github.klee0kai.test.inject.identity

import java.util.*

class Conscience {
    var uuid = UUID.randomUUID()
    val isOldConscience: Boolean
        get() = false
}