package com.github.klee0kai.stone.test.bindinstance.simple_inject

import com.github.klee0kai.test.di.bindinstance.simple_inject.SevenPlanetComponentStoneComponent
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SevenPlanetTests {

    @Test
    fun moonSkyTest() {
        //Given
        val component = SevenPlanetComponentStoneComponent()
        val earth = Earth()
        val mercury = Mercury()
        val planet = Earth()
        component.bind(earth)
        component.bind(mercury)
        component.bindPlanet(planet)

        //When
        val moonSky = MoonSky()
        component.inject(moonSky)

        //Then
        assertEquals(earth, moonSky.earth)
        assertEquals(mercury, moonSky.mercury)
        assertEquals(planet, moonSky.planet)
    }

}
