package com.github.klee0kai.test.house.rooms;

import com.github.klee0kai.test.house.kitchen.storagearea.GarageStore;

import java.util.UUID;

public class Garage {

    public UUID uuid = UUID.randomUUID();

    public final GarageStore garageStore;


    public Garage(GarageStore garageStore) {
        this.garageStore = garageStore;
    }
}
