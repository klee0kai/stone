package com.github.klee0kai.test.house.kitchen

import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import java.util.*

class Kichen(
    @JvmField val cookingArea: CookingArea?,
    @JvmField val sinkArea: SinkArea?,
    @JvmField val storeArea: StoreArea?
) {
    @JvmField
    var uuid: UUID = UUID.randomUUID()
}
