@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.body

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Blood(color: Int?) {

    var uuid: String = Uuid.random().toString()

    var color: Int? = null

    init {
        this.color = color
    }
}
