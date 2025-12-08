package com.github.klee0kai.test.house;

import com.github.klee0kai.test.house.kitchen.Kichen;
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea;
import com.github.klee0kai.test.house.rooms.BathRoom;
import com.github.klee0kai.test.house.rooms.Garage;

import javax.inject.Inject;

public class InHouse {

    @Inject
    public Kichen kichen;

    @Inject
    public BathRoom bathRoom;

    @Inject
    public BathRoom bedRoom;

    @Inject
    public Garage garage;

    @Inject
    public StoreArea bedStoreArea;

}
