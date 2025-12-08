package com.github.klee0kai.test.house;

import com.github.klee0kai.test.house.kitchen.Kichen;
import com.github.klee0kai.test.house.rooms.BathRoom;
import com.github.klee0kai.test.house.rooms.BedRoom;
import com.github.klee0kai.test.house.rooms.Garage;

import java.util.UUID;

public class House {

    public UUID uuid = UUID.randomUUID();

    public final Kichen kichen;
    public final BathRoom bathRoom;
    public final BedRoom bedRoom;
    public final Garage garage;

    public House(Kichen kichen, BathRoom bathRoom, BedRoom bedRoom, Garage garage) {
        this.kichen = kichen;
        this.bathRoom = bathRoom;
        this.bedRoom = bedRoom;
        this.garage = garage;
    }
}
