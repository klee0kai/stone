package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import java.util.*

open class Ram {
    @JvmField
    val uuid: UUID = UUID.randomUUID()

    @JvmField
    val size: String?

    constructor() {
        size = "default"
    }

    constructor(ramSize: RamSize?) {
        this.size = ramSize?.size
    }
}
