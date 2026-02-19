@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.tech.components

import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Battery {
    @JvmField
    var uuid: String = Uuid.random().toString()
}
