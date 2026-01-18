package com.dirgub.klee0kai.stone.text_ext.bindinstance.singlemethod

import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Sun
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.PlanetSputnikComponent
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.PlanetSputnikComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class StarProvideTests {

    @Test
    fun sunReusableTest() {
        //Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val sun = Sun()
        DI.sunModule()?.sun(sun)

        //When
        val DIPro: PlanetSputnikComponent = PlanetSputnikComponentStoneComponent()
        DIPro.extendOf(DI)


        //Then
        assertEquals(sun, DI.sunModule()!!.sun(null))
        assertEquals(sun, DI.sunModule()!!.sun(null))
        assertNull(DI.sunModule()!!.star(null))
        assertNull(DIPro.sunModule().star(null))
    }

    @Test
    fun createAfterExtendTest() {
        //Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val DIPro: PlanetSputnikComponent = PlanetSputnikComponentStoneComponent()
        DIPro.extendOf(DI)
        val sun = Sun()

        //When
        DI.sunModule()!!.sun(sun)


        //Then
        assertEquals(sun, DI.sunModule()!!.sun(null))
        assertEquals(sun, DI.sunModule()!!.sun(null))
        assertNull(DI.sunModule()!!.star(null))
        assertNull(DIPro.sunModule()!!.star(null))
    }


    @Test
    fun extendedSunTest() {
        //Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val sun1 = Sun()
        val sun2 = Sun()
        DI.sunModule()!!.sun(sun1)

        //When
        val DIPro: PlanetSputnikComponent = PlanetSputnikComponentStoneComponent()
        DIPro.extendOf(DI)
        DIPro.sunModule().sun(sun2)

        //Then
        assertEquals(sun2, DI.sunModule()!!.sun(null))
        assertEquals(sun2, DI.sunModule()!!.sun(null))
        assertNull(DI.sunModule()!!.star(null))
        assertNull(DIPro.sunModule().star(null))
    }
}
