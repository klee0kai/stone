package com.github.klee0kai.test.di.house.simple

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.house.kitchen.Kichen
import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea
import com.github.klee0kai.test.house.kitchen.storagearea.GarageStore
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea
import com.github.klee0kai.test.house.rooms.BathRoom
import com.github.klee0kai.test.house.rooms.BedRoom
import com.github.klee0kai.test.house.rooms.Garage

@Module
interface RoomsModule {
    @Provide(cache = Provide.CacheType.Soft)
    fun kitchen(cookingArea: CookingArea?, sinkArea: SinkArea?, storeArea: StoreArea?): Kichen?

    @Provide(cache = Provide.CacheType.Soft)
    fun bathRoom(storeArea: StoreArea?): BathRoom?

    @Provide(cache = Provide.CacheType.Soft)
    fun bedRoom(storeArea: StoreArea?): BedRoom?

    @Provide(cache = Provide.CacheType.Soft)
    fun garage(garageStore: GarageStore?): Garage?
}
