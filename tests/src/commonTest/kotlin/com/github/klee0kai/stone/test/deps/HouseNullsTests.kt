package com.github.klee0kai.stone.test.deps

import com.github.klee0kai.test.di.house.nulls.HouseNullsComponentStoneComponent
import kotlin.test.Test
import kotlin.test.assertNull

class HouseNullsTests {

    @Test
    fun buildOnlyKitchenTest() {
        //Given
        val di = HouseNullsComponentStoneComponent()

        //when
        val house = di.house()

        //then
        assertNull(house?.bathRoom)
        assertNull(house?.kichen?.cookingArea)
        assertNull(house?.kichen?.storeArea)
        assertNull(house?.kichen?.sinkArea)
    }

}
