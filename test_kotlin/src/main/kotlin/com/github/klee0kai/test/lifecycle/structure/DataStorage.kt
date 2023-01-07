package com.github.klee0kai.test.lifecycle.structure

import com.github.klee0kai.test.lifecycle.di.qualifier.DataStorageSize
import java.util.*

class DataStorage {
    val uuid = UUID.randomUUID()
    val size: String?

    constructor() {
        size = null
    }

    constructor(size: DataStorageSize) {
        this.size = size.size
    }
}