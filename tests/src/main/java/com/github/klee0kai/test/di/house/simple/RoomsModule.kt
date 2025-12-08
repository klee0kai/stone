package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.module.Module;
import com.github.klee0kai.stone.annotations.module.Provide;
import com.github.klee0kai.test.house.kitchen.Kichen;
import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea;
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea;
import com.github.klee0kai.test.house.kitchen.storagearea.GarageStore;
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea;
import com.github.klee0kai.test.house.rooms.BathRoom;
import com.github.klee0kai.test.house.rooms.BedRoom;
import com.github.klee0kai.test.house.rooms.Garage;

@Module
public interface RoomsModule {

    @Provide(cache = Provide.CacheType.Soft)
    Kichen kitchen(CookingArea cookingArea, SinkArea sinkArea, StoreArea storeArea);

    @Provide(cache = Provide.CacheType.Soft)
    BathRoom bathRoom(StoreArea storeArea);

    @Provide(cache = Provide.CacheType.Soft)
    BedRoom bedRoom(StoreArea storeArea);

    @Provide(cache = Provide.CacheType.Soft)
    Garage garage(GarageStore garageStore);

}
