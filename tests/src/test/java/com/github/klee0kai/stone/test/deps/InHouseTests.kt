package com.github.klee0kai.stone.test.deps;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.house.simple.HouseComponent;
import com.github.klee0kai.test.house.InHouse;
import com.github.klee0kai.test.house.identifiers.StoreAreaType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InHouseTests {

    @Test
    public void buildHouseTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);
        InHouse inHouse = new InHouse();

        //when
        di.inject(inHouse);

        //then
        assertNotNull(inHouse.bathRoom);
        assertNotNull(inHouse.kichen.cookingArea);
        assertNotNull(inHouse.kichen.storeArea);
        assertNotNull(inHouse.kichen.sinkArea);
        assertNotNull(inHouse.bedStoreArea);
    }

    @Test
    public void identifiersTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);
        InHouse inHouse = new InHouse();

        //when
        di.inject(StoreAreaType.CLOSED,inHouse);


        //then
        assertEquals(StoreAreaType.CLOSED, inHouse.kichen.storeArea.type);
    }

    @Test
    public void identifiersNullTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);
        InHouse inHouse = new InHouse();

        //when
        di.inject(inHouse);


        //then
        assertNull(inHouse.kichen.storeArea.type);
    }


    @Test
    public void cacheTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);
        InHouse inHouse1 = new InHouse();
        InHouse inHouse2 = new InHouse();

        //when
        di.inject(inHouse1);
        di.inject(inHouse2);

        //then
        assertEquals(inHouse1.kichen.uuid, inHouse2.kichen.uuid);
    }


    @Test
    public void factoryTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);
        InHouse inHouse1 = new InHouse();
        InHouse inHouse2 = new InHouse();

        //when
        di.inject(inHouse1);
        di.inject(inHouse2);

        //then
        assertNotEquals(inHouse1.kichen.storeArea.sanitizers,inHouse2.bathRoom.storeArea.sanitizers);
        assertNotEquals(inHouse1.kichen.storeArea.sanitizers,inHouse2.bedRoom.storeArea.sanitizers);
    }

}
