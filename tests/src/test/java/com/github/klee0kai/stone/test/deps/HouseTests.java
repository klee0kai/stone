package com.github.klee0kai.stone.test.deps;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.house.HouseComponent;
import com.github.klee0kai.test.house.House;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
