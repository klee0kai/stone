package com.github.klee0kai.test.lifecycle.structure

import com.github.klee0kai.test.lifecycle.di.qualifier.RamSize
import java.util.*

class Ram {
    val uuid = UUID.randomUUID()
    val size: String?

    constructor() {
        size = null
    }

    constructor(ramSize: RamSize) {
        size = ramSize.size
    }
}