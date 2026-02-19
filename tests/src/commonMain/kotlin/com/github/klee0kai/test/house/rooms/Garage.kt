@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.rooms

import com.github.klee0kai.test.house.kitchen.storagearea.GarageStore
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Garage(val garageStore: GarageStore?) {
    var uuid: String = Uuid.random().toString()
}
