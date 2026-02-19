package com.github.klee0kai.stone.test.deps

import com.github.klee0kai.test.di.house.simple.HouseComponentStoneComponent
import com.github.klee0kai.test.house.InHouse
import com.github.klee0kai.test.house.identifiers.StoreAreaType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class InHouseTests {

    @Test
    fun buildHouseTest() {
        //Given
        val di = HouseComponentStoneComponent()
        val inHouse = InHouse()

        //when
        di.inject(inHouse)

        //then
        assertNotNull(inHouse.bathRoom)
        assertNotNull(inHouse.kichen?.cookingArea)
        assertNotNull(inHouse.kichen?.storeArea)
        assertNotNull(inHouse.kichen?.sinkArea)
        assertNotNull(inHouse.bedStoreArea)
    }

    @Test
    fun identifiersTest() {
        //Given
        val di = HouseComponentStoneComponent()
        val inHouse = InHouse()

        //when
        di.inject(StoreAreaType.CLOSED, inHouse)


        //then
        assertEquals(StoreAreaType.CLOSED, inHouse.kichen?.storeArea?.type)
    }

    @Test
    fun identifiersNullTest() {
        //Given
        val di = HouseComponentStoneComponent()
        val inHouse = InHouse()

        //when
        di.inject(inHouse)


        //then
        assertNull(inHouse.kichen?.storeArea?.type)
    }


    @Test
    fun cacheTest() {
        //Given
        val di = HouseComponentStoneComponent()
        val inHouse1 = InHouse()
        val inHouse2 = InHouse()

        //when
        di.inject(inHouse1)
        di.inject(inHouse2)

        //then
        assertEquals(
            inHouse1.kichen?.uuid,
            inHouse2.kichen?.uuid
        )
    }


    @Test
    fun factoryTest() {
        //Given
        val di = HouseComponentStoneComponent()
        val inHouse1 = InHouse()
        val inHouse2 = InHouse()

        //when
        di.inject(inHouse1)
        di.inject(inHouse2)

        //then
        assertNotEquals(
            inHouse1.kichen?.storeArea?.sanitizers,
            inHouse2.bathRoom?.storeArea?.sanitizers,
        )
        assertNotEquals(
            inHouse1.kichen?.storeArea?.sanitizers,
            inHouse2.bedRoom?.storeArea?.sanitizers,
        )
    }

}
