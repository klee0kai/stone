package com.github.klee0kai.stone.test.deps

import com.github.klee0kai.test.di.house.simple.HouseComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class HouseFactoryTests {


    @Test
    fun cacheTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house1 = di.module().house(null, null, null, null)
        val house2 = di.module().house(null, null, null, null)

        //then
        assertEquals(house1?.uuid, house2?.uuid)
    }


    @Test
    fun factoryTest() {
        //Given
        val di = HouseComponentStoneComponent()

        //when
        val house1 = di.moduleFactory()?.house(null, null, null, null)
        val house2 = di.moduleFactory()?.house(null, null, null, null)

        //then
        assertNotEquals(house1?.uuid, house2?.uuid)
    }

}
