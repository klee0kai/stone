package com.github.klee0kai.stone.test.bindinstance.singlemethod

import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponentStoneComponent
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlanetProvideTests {

    @Test
    fun bindPlanetTest() {
        //Given
        val DI = PlanetComponentStoneComponent()
        val earth = Earth()

        //When
        DI.planet(earth)

        //Then
        assertEquals(earth, DI.planet(null))
        assertEquals(earth, DI.planet(null))
        assertEquals(earth, DI.providePlanet())
        assertEquals(earth, DI.providePlanet())
        assertNull(DI.earth(null))
    }

    @Test
    fun bindEarthTest() {
        //Given
        val DI = PlanetComponentStoneComponent()
        val earth = Earth()

        //When
        DI.earth(earth)

        //Then
        assertEquals(earth, DI.earth(null))
        assertEquals(earth, DI.earth(null))
        assertNull(DI.planet(null))
        assertNull(DI.providePlanet())
    }

    @Test
    fun bindEarthCommonTest() {
        //Given
        val DI = PlanetComponentStoneComponent()
        val earth = Earth()

        //When
        DI.earth(earth)

        //Then
        assertEquals(earth, DI.earth(null))
        assertEquals(earth, DI.earth(null))
        assertNull(DI.planet(null))
        assertNull(DI.providePlanet())
    }


    @Test
    fun separateBindEarthTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        val earth3 = Earth()

        //When
        DI.earthStrong(earth1)
        DI.earthSoft(earth2)
        DI.earth(earth3)

        //Then
        assertEquals(earth1, DI.earthStrong(null))
        assertEquals(earth2, DI.earthSoft(null))
        assertEquals(earth3, DI.earth(null))
    }
}
