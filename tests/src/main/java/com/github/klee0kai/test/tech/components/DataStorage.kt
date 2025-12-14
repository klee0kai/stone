package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import java.util.*

class DataStorage {
    @JvmField
    val uuid: UUID = UUID.randomUUID()

    @JvmField
    val size: String?

    constructor() {
        size = null
    }

    constructor(size: DataStorageSize?) {
        this.size = size?.size
    }
}
