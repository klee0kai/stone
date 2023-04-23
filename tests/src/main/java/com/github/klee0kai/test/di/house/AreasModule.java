package com.github.klee0kai.test.di.house;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea;
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea;
import com.github.klee0kai.test.house.kitchen.storagearea.Cookware;
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea;

@Module
public interface AreasModule {

    CookingArea cookingArea();

    SinkArea sinkArea();

    StoreArea storeArea(Cookware cookware);

}
