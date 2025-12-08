package com.github.klee0kai.test.house

import com.github.klee0kai.test.house.kitchen.Kichen
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import com.github.klee0kai.test.house.rooms.BathRoom
import com.github.klee0kai.test.house.rooms.Garage
import javax.inject.Inject

class InHouse {
    @JvmField
    @Inject
    var kichen: Kichen? = null

    @JvmField
    @Inject
    var bathRoom: BathRoom? = null

    @JvmField
    @Inject
    var bedRoom: BathRoom? = null

    @JvmField
    @Inject
    var garage: Garage? = null

    @JvmField
    @Inject
    var bedStoreArea: StoreArea? = null
}
