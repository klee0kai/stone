package com.github.klee0kai.test.di.house.simple

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.house.House
import com.github.klee0kai.test.house.kitchen.Kichen
import com.github.klee0kai.test.house.rooms.BathRoom
import com.github.klee0kai.test.house.rooms.BedRoom
import com.github.klee0kai.test.house.rooms.Garage

@Module
interface HouseModule {
    @Provide(cache = Provide.CacheType.Soft)
    fun house(kichen: Kichen?, bathRoom: BathRoom?, bedRoom: BedRoom?, garage: Garage?): House?
}
