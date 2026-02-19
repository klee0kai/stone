@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

open class Ram {
    open val uuid: String = Uuid.random().toString()


    val size: String?

    constructor() {
        size = "default"
    }

    constructor(ramSize: RamSize?) {
        this.size = ramSize?.size
    }
}
