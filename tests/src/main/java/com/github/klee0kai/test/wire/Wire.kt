package com.github.klee0kai.test.wire

import java.util.*

class Wire<Input, Output> {

    val uuid: UUID = UUID.randomUUID()

    var input: Input? = null

    var output: Output? = null
}
