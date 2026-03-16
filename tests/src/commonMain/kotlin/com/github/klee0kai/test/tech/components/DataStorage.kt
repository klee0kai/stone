@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.tech.components

import com.github.klee0kai.test.di.base_phone.identifiers.DataStorageSize
import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DataStorage {
    @JvmField
    var uuid: String = Uuid.random().toString()

    @JvmField
    val size: String?

    constructor() {
        size = null
    }

    constructor(size: DataStorageSize?) {
        this.size = size?.size
    }
}
