@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.wire

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Wire<Input, Output> {

    val uuid: String = Uuid.random().toString()

    var input: Input? = null

    var output: Output? = null
}
