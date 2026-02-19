@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.identity

import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Conscience {
    @JvmField
    var uuid: String = Uuid.random().toString()

    val isOldConscience: Boolean
        get() = false
}
