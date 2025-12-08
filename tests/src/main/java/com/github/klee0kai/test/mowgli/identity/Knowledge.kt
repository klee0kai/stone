package com.github.klee0kai.test.mowgli.identity

import java.util.*

open class Knowledge {
    @JvmField
    var uuid: UUID = UUID.randomUUID()


    open val isOldKnowledge: Boolean
        get() = false
}
