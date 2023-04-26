package com.github.klee0kai.test.house.rooms;

import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea;

import java.util.UUID;

public class BedRoom {

    public UUID uuid = UUID.randomUUID();

    public final StoreArea storeArea;


    public BedRoom(StoreArea storeArea) {
        this.storeArea = storeArea;
    }
}
