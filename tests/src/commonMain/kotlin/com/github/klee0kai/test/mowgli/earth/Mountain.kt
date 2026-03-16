@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.earth

import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Mountain : IMountain {

    @JvmField
    var uuid: String = Uuid.random().toString()

}
