package com.github.klee0kai.tests.java_models.bindinstance.singlemethod

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class PlanetProvideTests {
    @Test
    fun bindPlanetTest() {
        //Given
        val DI = Stone.createComponent(PlanetComponent::class.java)
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
        val DI = Stone.createComponent(PlanetComponent::class.java)
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
        val DI = Stone.createComponent(PlanetComponent::class.java)
        val earth = Earth()

        //When
        DI.earth(earth)

        //Then
        assertEquals(earth, DI.earth(null))
        assertEquals(earth, DI.earth(null))
        assertNull(DI.planet(null))
        assertNull(DI.providePlanet())
    }
}