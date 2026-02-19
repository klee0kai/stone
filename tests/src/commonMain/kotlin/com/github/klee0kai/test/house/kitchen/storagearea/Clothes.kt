@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.kitchen.storagearea

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Clothes {
    var uuid: String = Uuid.random().toString()
}
