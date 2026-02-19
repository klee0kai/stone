package com.github.klee0kai.test.house

import com.github.klee0kai.stone.weakref.Inject
import com.github.klee0kai.test.house.kitchen.Kichen
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import com.github.klee0kai.test.house.rooms.BathRoom
import com.github.klee0kai.test.house.rooms.Garage

class InHouse {

    @Inject
    var kichen: Kichen? = null

    @Inject
    var bathRoom: BathRoom? = null

    @Inject
    var bedRoom: BathRoom? = null

    @Inject
    var garage: Garage? = null

    @Inject
    var bedStoreArea: StoreArea? = null
}
