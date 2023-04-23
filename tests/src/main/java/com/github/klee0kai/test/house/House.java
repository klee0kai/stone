package com.github.klee0kai.test.house;

import com.github.klee0kai.test.house.kitchen.Kichen;
import com.github.klee0kai.test.house.rooms.BathRoom;
import com.github.klee0kai.test.house.rooms.Garage;

import java.util.UUID;

public class House {

    public UUID uuid = UUID.randomUUID();

    public final Kichen kichen;
    public final BathRoom bathRoom;
    public final Garage garage;

    public House(Kichen kichen, BathRoom bathRoom, Garage garage) {
        this.kichen = kichen;
        this.bathRoom = bathRoom;
        this.garage = garage;
    }
}
