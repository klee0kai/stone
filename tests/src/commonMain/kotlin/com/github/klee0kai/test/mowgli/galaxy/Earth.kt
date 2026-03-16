@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.galaxy

import kotlin.jvm.JvmField
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Earth : IPlanet {
    @JvmField
    var uuid: String = Uuid.random().toString()
}
