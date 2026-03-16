package com.dirgub.klee0kai.stone.text_ext.bindinstance.simple

import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test_ext.inject.di.bindinstance.simple.GodRebrandingComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class GodRebrandingPlanetTest {

    @Test
    fun createdIsReusableTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val earth = Earth()
        DI.bindPlanet(earth)


        //When
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)


        //Then
        assertEquals(earth, DI.sunSystem().planet())
        assertEquals(earth, DIPro.sunSystem().planet())
        assertNull(DI.sunSystem().earth())
        assertNull(DIPro.sunSystem().earth())
    }

    @Test
    fun createdAfterRebrandingTest() {
        // Given
        val DI = GodRebrandingComponentStoneComponent()
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)
        val earth = Earth()


        //When
        DI.bindPlanet(earth)


        //Then
        assertEquals(earth, DI.sunSystem().planet())
        assertEquals(earth, DIPro.sunSystem().planet())
        assertNull(DI.sunSystem().earth())
        assertNull(DIPro.sunSystem().earth())
    }


    @Test
    fun rebrandedSunTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        DI.bindPlanet(earth1)


        //When
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)
        DIPro.bindPlanet(earth2)


        //Then
        assertEquals(earth2, DI.sunSystem().planet())
        assertEquals(earth2, DIPro.sunSystem().planet())
        assertNull(DI.sunSystem().earth())
        assertNull(DIPro.sunSystem().earth())
    }


    @Test
    fun updatedAfterRebrandingSunTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        val earth3 = Earth()
        DI.bindPlanet(earth1)
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)
        DIPro.bindPlanet(earth2)


        //When
        DI.bindPlanet(earth3)


        //Then
        assertEquals(earth3, DI.sunSystem().planet())
        assertEquals(earth3, DIPro.sunSystem().planet())
        assertNull(DI.sunSystem().earth())
        assertNull(DIPro.sunSystem().earth())
    }

}
