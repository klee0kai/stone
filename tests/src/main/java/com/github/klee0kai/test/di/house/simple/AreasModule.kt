package com.github.klee0kai.test.di.house.simple

import com.github.klee0kai.stone.annotations.module.Module
import com.github.klee0kai.stone.annotations.module.Provide
import com.github.klee0kai.test.house.identifiers.StoreAreaType
import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea
import com.github.klee0kai.test.house.kitchen.storagearea.*

@Module
abstract class AreasModule {
    @Provide(cache = Provide.CacheType.Soft)
    abstract fun cookingArea(): CookingArea?

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun sinkArea(): SinkArea?

    @Provide(cache = Provide.CacheType.Factory)
    abstract fun storeArea(
        type: StoreAreaType?,
        cookware: Cookware?,
        clothes: Clothes?,
        sanitizers: Sanitizers?
    ): StoreArea?

    @Provide(cache = Provide.CacheType.Soft)
    abstract fun garageStore(cookware: Cookware?, clothes: Clothes?, sanitizers: Sanitizers?): GarageStore?
}
