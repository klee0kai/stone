package com.github.klee0kai.test.bindinstance

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.bindinstance.GodWorkspaceComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Saturn
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class GodFirstWorkDayTest {
    @Test
    fun firstCreateSunTest() {
        //Given
        val DI = Stone.createComponent(GodWorkspaceComponent::class.java)
        val sun = Sun()

        //When
        DI.bind(sun)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid)
        assertNull(DI.sunSystem().earth())
    }

    @Test
    fun createSunAndEarthTest() {
        //Given
        val DI = Stone.createComponent(GodWorkspaceComponent::class.java)
        val sun = Sun()
        val earth = Earth()

        //When
        DI.bind(sun, earth)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid)
        assertNull(DI.sunSystem().planet())
    }

    @Test
    fun createSunEarthSaturnTest() {
        //Given
        val DI = Stone.createComponent(GodWorkspaceComponent::class.java)
        val sun = Sun()
        val earth = Earth()
        val saturn = Saturn()

        //When
        DI.bind(sun, earth, saturn)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid)
        assertEquals(saturn.uuid, DI.sunSystem().saturn().uuid)
        assertNull(DI.sunSystem().planet())
    }

    @Test
    fun createSaturnEarthSunTest() {
        //Given
        val DI = Stone.createComponent(GodWorkspaceComponent::class.java)
        val sun = Sun()
        val earth = Earth()
        val saturn = Saturn()

        //When
        DI.bind(saturn)
        DI.bind(earth)
        DI.bind(sun)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid)
        assertEquals(saturn.uuid, DI.sunSystem().saturn().uuid)
        assertNull(DI.sunSystem().planet())
    }
}