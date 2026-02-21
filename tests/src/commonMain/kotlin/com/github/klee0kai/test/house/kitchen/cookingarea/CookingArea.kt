@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.kitchen.cookingarea

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CookingArea {
    var uuid: String = Uuid.random().toString()
}
