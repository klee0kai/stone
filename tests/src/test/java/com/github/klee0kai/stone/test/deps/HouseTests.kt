package com.github.klee0kai.stone.test.deps

import com.github.klee0kai.test.di.house.simple.HouseComponentStoneComponent
import com.github.klee0kai.test.house.House
import com.github.klee0kai.test.house.identifiers.StoreAreaType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull

class HouseTests {

    @Test
    fun buildHouseTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house = di.house()

        //then
        assertNotNull(house?.bathRoom)
        assertNotNull(house?.kichen?.cookingArea)
        assertNotNull(house?.kichen?.storeArea)
        assertNotNull(house?.kichen?.sinkArea)
    }

    @Test
    fun identifiersTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house = di.house(StoreAreaType.CLOSED)

        //then
        assertEquals(StoreAreaType.CLOSED, house?.kichen?.storeArea?.type)
    }

    @Test
    fun identifiersNullTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house = di.house()

        //then
        assertNull(house?.kichen?.storeArea?.type)
    }


    @Test
    fun cacheTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house1: House? = di.house()
        val house2: House? = di.house()

        //then
        assertEquals(
            house1?.kichen?.uuid,
            house2?.kichen?.uuid
        )
    }


    @Test
    fun factoryTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house: House? = di.house()
        val sanitizers1 = di.sanitizers()
        val sanitizers2 = di.sanitizers()

        //then
        assertNotEquals(sanitizers1?.uuid, sanitizers2?.uuid)
        assertNotEquals(
            house?.kichen?.storeArea?.sanitizers,
            house?.bathRoom?.storeArea?.sanitizers
        )
        assertNotEquals(
            house?.kichen?.storeArea?.sanitizers,
            house?.bedRoom?.storeArea?.sanitizers
        )
    }
}
