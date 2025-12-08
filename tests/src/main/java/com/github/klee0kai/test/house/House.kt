package com.github.klee0kai.test.house

import com.github.klee0kai.test.house.kitchen.Kichen
import com.github.klee0kai.test.house.rooms.BathRoom
import com.github.klee0kai.test.house.rooms.BedRoom
import com.github.klee0kai.test.house.rooms.Garage
import java.util.*

class House(@JvmField val kichen: Kichen?, @JvmField val bathRoom: BathRoom?, @JvmField val bedRoom: BedRoom?, val garage: Garage?) {
    @JvmField
    var uuid: UUID = UUID.randomUUID()
}
