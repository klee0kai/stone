package com.github.klee0kai.stone.test.deps;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.house.nulls.HouseNullsComponent;
import com.github.klee0kai.test.house.House;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class HouseNullsTests {

    @Test
    public void buildOnlyKitchenTest() {
        //Given
        HouseNullsComponent di = Stone.createComponent(HouseNullsComponent.class);

        //when
        House house = di.house();

        //then
        assertNull(house.bathRoom);
        assertNull(house.kichen.cookingArea);
        assertNull(house.kichen.storeArea);
        assertNull(house.kichen.sinkArea);
    }

}
