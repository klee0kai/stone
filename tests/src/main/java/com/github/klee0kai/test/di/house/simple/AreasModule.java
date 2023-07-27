package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.house.identifiers.StoreAreaType;
import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea;
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea;
import com.github.klee0kai.test.house.kitchen.storagearea.*;

@Module
abstract class AreasModule {

    @Provide(cache = Provide.CacheType.Soft)
    public abstract CookingArea cookingArea();

    @Provide(cache = Provide.CacheType.Soft)
    public abstract SinkArea sinkArea();

    @Provide(cache = Provide.CacheType.Factory)
    public abstract StoreArea storeArea(StoreAreaType type, Cookware cookware, Clothes clothes, Sanitizers sanitizers);

    @Provide(cache = Provide.CacheType.Soft)
    abstract public GarageStore garageStore(Cookware cookware, Clothes clothes, Sanitizers sanitizers);

}
