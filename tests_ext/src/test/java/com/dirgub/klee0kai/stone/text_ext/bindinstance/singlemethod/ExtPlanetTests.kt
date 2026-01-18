package com.dirgub.klee0kai.stone.text_ext.bindinstance.singlemethod

import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.ExtPlanetComponent
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.ExtPlanetComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class ExtPlanetTests {

    @Test
    fun createdIsReusableTest() {
        // Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val earth = Earth()
        DI.earth(earth)


        //When
        val DIPro: ExtPlanetComponent = ExtPlanetComponentStoneComponent()
        DIPro.extendOf(DI)


        //Then
        assertEquals(earth, DI.earth(null))
        assertEquals(earth, DIPro.earth(null))
        assertNull(DI.planet(null))
        assertNull(DIPro.planet(null))
    }

    @Test
    fun createdAfterExtendTest() {
        // Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val DIPro: ExtPlanetComponent = ExtPlanetComponentStoneComponent()
        DIPro.extendOf(DI)
        val earth = Earth()


        //When
        DI.earth(earth)


        //Then
        assertEquals(earth, DI.earth(null))
        assertEquals(earth, DIPro.earth(null))
        assertNull(DI.planet(null))
        assertNull(DIPro.planet(null))
    }


    @Test
    fun extendedEarthTest() {
        // Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        DI.earth(earth1)


        //When
        val DIPro: ExtPlanetComponent = ExtPlanetComponentStoneComponent()
        DIPro.extendOf(DI)
        DIPro.earth(earth2)


        //Then
        assertEquals(earth2, DI.earth(null))
        assertEquals(earth2, DIPro.earth(null))
        assertNull(DI.planet(null))
        assertNull(DIPro.planet(null))
    }


    @Test
    fun updatedAfterExtendEarthTest() {
        // Given
        val DI: PlanetComponent = PlanetComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        val earth3 = Earth()
        DI.earth(earth1)
        val DIPro: ExtPlanetComponent = ExtPlanetComponentStoneComponent()
        DIPro.extendOf(DI)
        DIPro.earth(earth2)


        //When
        DI.earth(earth3)


        //Then
        assertEquals(earth3, DI.earth(null))
        assertEquals(earth3, DIPro.earth(null))
        assertNull(DI.planet(null))
        assertNull(DIPro.planet(null))
    }
}
