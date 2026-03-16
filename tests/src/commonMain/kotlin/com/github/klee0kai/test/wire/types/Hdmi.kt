@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.wire.types

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


class Hdmi {
    val uuid: String = Uuid.random().toString()
}
