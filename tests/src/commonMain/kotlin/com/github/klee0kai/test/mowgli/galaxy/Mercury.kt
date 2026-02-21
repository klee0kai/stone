@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.mowgli.galaxy

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Mercury : IPlanet {
    var uuid: String = Uuid.random().toString()
}
