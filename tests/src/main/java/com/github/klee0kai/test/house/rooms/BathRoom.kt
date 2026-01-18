package com.github.klee0kai.test.house.rooms

import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import java.util.*

class BathRoom(@JvmField val storeArea: StoreArea?) {
    var uuid: UUID = UUID.randomUUID()
}
