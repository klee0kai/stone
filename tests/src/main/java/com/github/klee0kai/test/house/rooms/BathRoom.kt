package com.github.klee0kai.test.house.rooms;

import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea;

import java.util.UUID;

public class BathRoom {

    public UUID uuid = UUID.randomUUID();

    public final StoreArea storeArea;


    public BathRoom(StoreArea storeArea) {
        this.storeArea = storeArea;
    }
}
