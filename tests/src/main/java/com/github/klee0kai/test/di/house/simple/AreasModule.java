package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea;
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea;
import com.github.klee0kai.test.house.kitchen.storagearea.*;
import com.github.klee0kai.test.house.qualifiers.StoreAreaType;

@Module
abstract class AreasModule {

    public abstract CookingArea cookingArea();

    public abstract SinkArea sinkArea();

    @Provide(cache = Provide.CacheType.Factory)
    public abstract StoreArea storeArea(StoreAreaType type, Cookware cookware, Clothes clothes, Sanitizers sanitizers);

    abstract public GarageStore garageStore(Cookware cookware, Clothes clothes, Sanitizers sanitizers);

}
