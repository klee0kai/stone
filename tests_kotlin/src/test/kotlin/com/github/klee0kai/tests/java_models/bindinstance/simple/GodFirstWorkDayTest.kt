package com.github.klee0kai.tests.java_models.bindinstance.simple

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponent
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
        DI.bindSun(sun)

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
        DI.bindSun(sun)
        DI.bindEarth(earth)

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
        DI.bindSun(sun)
        DI.bindEarth(earth)
        DI.bindSaturn(saturn)

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
        DI.bindSaturn(saturn)
        DI.bindEarth(earth)
        DI.bindSun(sun)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid)
        assertEquals(saturn.uuid, DI.sunSystem().saturn().uuid)
        assertNull(DI.sunSystem().planet())
    }
}