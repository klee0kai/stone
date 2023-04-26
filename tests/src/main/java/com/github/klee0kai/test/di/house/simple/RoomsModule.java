package com.github.klee0kai.test.di.house.simple;

import com.github.klee0kai.stone.annotations.module.Module;
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

    Kichen kitchen(CookingArea cookingArea, SinkArea sinkArea, StoreArea storeArea);

    BathRoom bathRoom(StoreArea storeArea);

    BedRoom bedRoom(StoreArea storeArea);


    Garage garage(GarageStore garageStore);

}
