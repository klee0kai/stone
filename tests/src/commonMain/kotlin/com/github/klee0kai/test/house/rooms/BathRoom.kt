@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house.rooms

import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BathRoom(val storeArea: StoreArea?) {
    var uuid: String = Uuid.random().toString()
}
