package com.github.klee0kai.test.house.kitchen;

import com.github.klee0kai.test.house.kitchen.cookingarea.CookingArea;
import com.github.klee0kai.test.house.kitchen.sinkarea.SinkArea;
import com.github.klee0kai.test.house.kitchen.storagearea.StoreArea;

import java.util.UUID;

public class Kichen {

    public UUID uuid = UUID.randomUUID();

    public final CookingArea cookingArea;
    public final SinkArea sinkArea;
    public final StoreArea storeArea;


    public Kichen(CookingArea cookingArea, SinkArea sinkArea, StoreArea storeArea) {
        this.cookingArea = cookingArea;
        this.sinkArea = sinkArea;
        this.storeArea = storeArea;
    }
}
