package com.github.klee0kai.stone.test.deps;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.house.simple.HouseComponent;
import com.github.klee0kai.test.house.House;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HouseFactoryTests {

    @Test
    public void cacheTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house1 = di.module().house(null, null, null, null);
        House house2 = di.module().house(null, null, null, null);

        //then
        assertEquals(house1.uuid, house2.uuid);
    }


    @Test
    public void factoryTest() {
        //Given
        HouseComponent di = Stone.createComponent(HouseComponent.class);

        //when
        House house1 = di.moduleFactory().house(null, null, null, null);
        House house2 = di.moduleFactory().house(null, null, null, null);

        //then
        assertNotEquals(house1.uuid, house2.uuid);
    }

}
