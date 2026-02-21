@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.kitchen.sinkarea

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SinkArea {
    var uuid: String = Uuid.random().toString()
}
