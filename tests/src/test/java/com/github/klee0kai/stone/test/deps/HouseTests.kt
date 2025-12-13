package com.github.klee0kai.stone.test.deps;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.house.simple.HouseComponent;
import com.github.klee0kai.test.house.House;
import com.github.klee0kai.test.house.kitchen.storagearea.Sanitizers;
import com.github.klee0kai.test.house.identifiers.StoreAreaType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HouseTests {

    @Test
    public void buildHouseTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house = di.house();

        //then
        assertNotNull(house.bathRoom);
        assertNotNull(house.kichen.cookingArea);
        assertNotNull(house.kichen.storeArea);
        assertNotNull(house.kichen.sinkArea);
    }

    @Test
    public void identifiersTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house = di.house(StoreAreaType.CLOSED);

        //then
        assertEquals(StoreAreaType.CLOSED, house.kichen.storeArea.type);
    }

    @Test
    public void identifiersNullTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house = di.house();

        //then
        assertNull(house.kichen.storeArea.type);
    }


    @Test
    public void cacheTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house1 = di.house();
        House house2 = di.house();

        //then
        assertEquals(house1.kichen.uuid, house2.kichen.uuid);
    }


    @Test
    public void factoryTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house = di.house();
        Sanitizers sanitizers1 = di.sanitizers();
        Sanitizers sanitizers2 = di.sanitizers();

        //then
        assertNotEquals(sanitizers1.uuid, sanitizers2.uuid);
        assertNotEquals(house.kichen.storeArea.sanitizers, house.bathRoom.storeArea.sanitizers);
        assertNotEquals(house.kichen.storeArea.sanitizers, house.bedRoom.storeArea.sanitizers);
    }


}
