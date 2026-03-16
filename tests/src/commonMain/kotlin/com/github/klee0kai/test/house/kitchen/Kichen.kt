@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.kitchen

import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Kichen(
    val cookingArea: CookingArea?,
    val sinkArea: SinkArea?,
    val storeArea: StoreArea?
) {
    var uuid: String = Uuid.random().toString()
}
