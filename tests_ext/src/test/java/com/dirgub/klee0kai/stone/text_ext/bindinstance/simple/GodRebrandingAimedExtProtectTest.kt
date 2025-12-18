package com.dirgub.klee0kai.stone.text_ext.bindinstance.simple

import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test_ext.inject.di.bindinstance.simple.GodRebrandingComponentStoneComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class GodRebrandingAimedExtProtectTest {

    @Test
    fun createdIsReusableTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val earth = Earth()
        DI.bindEarth(earth)


        //When
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)


        //Then
        assertEquals(earth, DI.sunSystem().earth())
        assertEquals(earth, DIPro.sunSystem().earth())
        assertNull(DI.sunSystem().planet())
        assertNull(DIPro.sunSystem().planet())
    }

    @Test
    fun createdAfterRebrandingTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)
        val earth = Earth()


        //When
        DI.bindEarth(earth)


        //Then
        assertEquals(earth, DI.sunSystem().earth())
        assertEquals(earth, DIPro.sunSystem().earth())
        assertNull(DI.sunSystem().planet())
        assertNull(DIPro.sunSystem().planet())
    }


    @Test
    fun rebrandedSunTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        DI.bindEarth(earth1)


        //When
        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)
        DIPro.bindEarth(earth2)


        //Then
        assertEquals(earth2, DI.sunSystem().earth())
        assertEquals(earth2, DIPro.sunSystem().earth())
        assertNull(DI.sunSystem().planet())
        assertNull(DIPro.sunSystem().planet())
    }


    @Test
    fun updatedAfterRebrandingSunTest() {
        // Given
        val DI = GodWorkspaceComponentStoneComponent()
        val earth1 = Earth()
        val earth2 = Earth()
        val earth3 = Earth()
        DI.bindEarth(earth1)

        val DIPro = GodRebrandingComponentStoneComponent()
        DIPro.extendComponent(DI)
        DIPro.bindEarth(earth2)


        //When
        DI.bindEarth(earth3)


        //Then
        assertEquals(earth3, DI.sunSystem().earth())
        assertEquals(earth3, DIPro.sunSystem().earth())
        assertNull(DI.sunSystem().planet())
        assertNull(DIPro.sunSystem().planet())
    }
}
