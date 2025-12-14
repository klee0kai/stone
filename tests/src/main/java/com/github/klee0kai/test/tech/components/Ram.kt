package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import java.util.*

open class Ram {
    open val uuid: UUID = UUID.randomUUID()

    val size: String?

    constructor() {
        size = "default"
    }

    constructor(ramSize: RamSize?) {
        this.size = ramSize?.size
    }
}
