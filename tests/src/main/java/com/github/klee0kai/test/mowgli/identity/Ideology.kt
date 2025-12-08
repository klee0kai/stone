package com.github.klee0kai.test.mowgli.identity

import java.util.*

open class Ideology {
    var uuid: UUID = UUID.randomUUID()


    open val isFamilyIdeology: Boolean
        get() = false
}
