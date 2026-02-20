package com.github.klee0kai.stone.test.bindinstance.solarsystem

import com.github.klee0kai.test.di.bindinstance.solarsystem.SolarSystemComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.SolarSystem
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SolarSystemTests {

    @Test
    fun provideSolarSystemTest() {
        //Given
        val DI = SolarSystemComponentStoneComponent()
        val solarSystem = SolarSystem()

        // When
        DI.bind(solarSystem)

        // Then
        assertEquals(solarSystem, DI.bind(null))
    }


    @Test
    fun provideEarthTest() {
        //Given
        val DI = SolarSystemComponentStoneComponent()
        val solarSystem = SolarSystem()

        // When
        DI.bind(solarSystem)

        // Then
        assertNotNull(DI.earth())
    }

}
