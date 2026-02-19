@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.earth

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

open class River : IRiver {
    open var uuid: String = Uuid.random().toString()
}
