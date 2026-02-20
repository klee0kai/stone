package com.github.klee0kai.stone.test.bindinstance.simple

import com.github.klee0kai.test.di.bindinstance.simple.genGodWorkspaceComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Saturn
import com.github.klee0kai.test.mowgli.galaxy.Sun
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GodFirstWorkDayTest {

    @Test
    fun firstCreateSunTest() {
        //Given
        val DI = genGodWorkspaceComponent()
        val sun = Sun()

        //When
        DI.bindSun(sun)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun()!!.uuid)
        assertNull(DI.sunSystem().earth())
    }


    @Test
    fun createSunAndEarthTest() {
        //Given
        val DI = genGodWorkspaceComponent()
        val sun = Sun()
        val earth = Earth()

        //When
        DI.bindSun(sun)
        DI.bindEarth(earth)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun()!!.uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth()!!.uuid)
        assertNull(DI.sunSystem().planet())
    }


    @Test
    fun createSunEarthSaturnTest() {
        //Given
        val DI = genGodWorkspaceComponent()
        val sun = Sun()
        val earth = Earth()
        val saturn = Saturn()

        //When
        DI.bindSun(sun)
        DI.bindEarth(earth)
        DI.bindSaturn(saturn)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun()!!.uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth()!!.uuid)
        assertEquals(saturn.uuid, DI.sunSystem().saturn()!!.uuid)
        assertNull(DI.sunSystem().planet())
    }


    @Test
    fun createSaturnEarthSunTest() {
        //Given
        val DI = genGodWorkspaceComponent()
        val sun = Sun()
        val earth = Earth()
        val saturn = Saturn()

        //When
        DI.bindSaturn(saturn)
        DI.bindEarth(earth)
        DI.bindSun(sun)

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun()!!.uuid)
        assertEquals(earth.uuid, DI.sunSystem().earth()!!.uuid)
        assertEquals(saturn.uuid, DI.sunSystem().saturn()!!.uuid)
        assertNull(DI.sunSystem().planet())
    }
}
