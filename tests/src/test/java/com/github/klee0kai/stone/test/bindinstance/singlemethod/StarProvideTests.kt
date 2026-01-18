package com.github.klee0kai.stone.test.bindinstance.singlemethod

import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class StarProvideTests {

    @Test
    fun bindSunTest() {
        //Given
        val DI = PlanetComponentStoneComponent()
        val sun = Sun()

        //When
        DI.sunModule().sun(sun)

        //Then
        assertEquals(sun, DI.sunModule().sun(null))
        assertEquals(sun, DI.sunModule().sun(null))
        assertNull(DI.sunModule().star(null))
    }

    @Test
    fun bindStarTest() {
        //Given
        val DI = PlanetComponentStoneComponent()
        val sun = Sun()

        //When
        DI.sunModule().star(sun)

        //Then
        assertEquals(sun, DI.sunModule().star(null))
        assertEquals(sun, DI.sunModule().star(null))
        assertNull(DI.sunModule().sun(null))
    }


    @Test
    fun separateSunBindingTest() {
        //Given
        val DI = PlanetComponentStoneComponent()
        val sun1 = Sun()
        val sun2 = Sun()
        val sun3 = Sun()

        //When
        DI.sunModule().sunStrong(sun1)
        DI.sunModule().sunSoft(sun2)
        DI.sunModule().sun(sun3)

        //Then
        assertEquals(sun1, DI.sunModule().sunStrong(null))
        assertEquals(sun2, DI.sunModule().sunSoft(null))
        assertEquals(sun3, DI.sunModule().sun(null))
    }

}
