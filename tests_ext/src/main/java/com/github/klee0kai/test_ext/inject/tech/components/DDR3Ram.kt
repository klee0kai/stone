@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test_ext.inject.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.RamSize
import com.github.klee0kai.test.tech.components.Ram
import com.github.klee0kai.test_ext.inject.di.techfactory.identifiers.Frequency
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DDR3Ram : Ram {
    override val uuid: String = Uuid.random().toString()

    val frequency: String?

    constructor() : super() {
        frequency = "default"
    }

    constructor(ramSize: RamSize?) : super(ramSize) {
        this.frequency = "default"
    }

    constructor(ramSize: RamSize?, frequency: Frequency) : super(ramSize) {
        this.frequency = frequency.frequency
    }
}
