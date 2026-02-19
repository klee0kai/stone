@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.identity

import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

open class Knowledge {

    @JvmField
    var uuid: String = Uuid.random().toString()


    open val isOldKnowledge: Boolean
        get() = false
}
