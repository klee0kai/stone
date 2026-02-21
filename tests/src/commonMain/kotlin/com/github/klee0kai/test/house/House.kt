@file:OptIn(ExperimentalUuidApi::class)

package com.github.klee0kai.test.house

import com.github.klee0kai.test.house.kitchen.Kichen
import com.github.klee0kai.test.house.rooms.BathRoom
import com.github.klee0kai.test.house.rooms.BedRoom
import com.github.klee0kai.test.house.rooms.Garage
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class House(
    val kichen: Kichen?,
    val bathRoom: BathRoom?,
    val bedRoom: BedRoom?,
    val garage: Garage?,
) {

    var uuid: String = Uuid.random().toString()

}
