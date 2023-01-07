package com.github.klee0kai.test.inject.identity

import java.util.*

class Knowledge {
    var uuid = UUID.randomUUID()
    val isOldKnowledge: Boolean
        get() = false
}