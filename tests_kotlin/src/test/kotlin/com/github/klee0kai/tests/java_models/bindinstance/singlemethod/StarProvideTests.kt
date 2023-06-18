package com.github.klee0kai.tests.java_models.bindinstance.singlemethod

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class StarProvideTests {
    @Test
    fun bindSunTest() {
        //Given
        val DI = Stone.createComponent(PlanetComponent::class.java)
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
        val DI = Stone.createComponent(PlanetComponent::class.java)
        val sun = Sun()

        //When
        DI.sunModule().star(sun)

        //Then
        assertEquals(sun, DI.sunModule().star(null))
        assertEquals(sun, DI.sunModule().star(null))
        assertNull(DI.sunModule().sun(null))
    }

}