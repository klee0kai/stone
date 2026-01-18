package com.github.klee0kai.stone.test.bindinstance.singlemethod_inject

import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponentStoneComponent
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertEquals

class MoonSkyTests {

    @org.junit.jupiter.api.Test
    fun moonSkyTest() {
        //Given
        val component = StarSkyComponentStoneComponent()
        val sun = Sun()
        val star = Sun()
        val earth = Earth()
        val planet = Mercury()
        component.starModule().sun(sun)
        component.starModule().star(star)
        component.earth(earth)
        component.planet(planet)

        //When
        val moonSky: MoonSky = MoonSky()
        component.inject(moonSky)

        //Then
        assertEquals(sun, moonSky.sun)
        assertEquals(star, moonSky.star)
        assertEquals(earth, moonSky.earth)
        assertEquals(planet, moonSky.planet)
    }
}
