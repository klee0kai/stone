package com.github.klee0kai.stone.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class WindowMappedReusableGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        //Then
        assertNotEquals(windowsFactory1, windowsFactory2)
        assertNotEquals(windowWeak1, windowWeak2)
        assertNotEquals(windowSoft1, windowSoft2)
        assertNotEquals(windowStrong1, windowStrong2)
    }

    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcAll()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertNotEquals(windowSoft1, windowSoft1Reuse)
        assertNotEquals(windowSoft2, windowSoft2Reuse)
        assertNotEquals(windowStrong1, windowStrong1Reuse)
        assertNotEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcWeak()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertEquals(windowSoft1, windowSoft1Reuse)
        assertEquals(windowSoft2, windowSoft2Reuse)
        assertEquals(windowStrong1, windowStrong1Reuse)
        assertEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcSoft()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertNotEquals(windowSoft1, windowSoft1Reuse)
        assertNotEquals(windowSoft2, windowSoft2Reuse)
        assertEquals(windowStrong1, windowStrong1Reuse)
        assertEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcStrong()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertEquals(windowSoft1, windowSoft1Reuse)
        assertEquals(windowSoft2, windowSoft2Reuse)
        assertNotEquals(windowStrong1, windowStrong1Reuse)
        assertNotEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcWindows() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcWindows()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertNotEquals(windowSoft1, windowSoft1Reuse)
        assertNotEquals(windowSoft2, windowSoft2Reuse)
        assertNotEquals(windowStrong1, windowStrong1Reuse)
        assertNotEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcWindowsAndWheels() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcWindowsAndWheels()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertNotEquals(windowSoft1, windowSoft1Reuse)
        assertNotEquals(windowSoft2, windowSoft2Reuse)
        assertNotEquals(windowStrong1, windowStrong1Reuse)
        assertNotEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcWheels()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertEquals(windowSoft1, windowSoft1Reuse)
        assertEquals(windowSoft2, windowSoft2Reuse)
        assertEquals(windowStrong1, windowStrong1Reuse)
        assertEquals(windowStrong2, windowStrong2Reuse)
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory1 = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1 = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1 = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1 = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2 = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2 = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2 = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2 = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }


        //When
        DI.gcNothing()
        val windowsFactory1Reuse = DI.windowsMappedModule()!!.windowFactory("1").get()!!.map { it.uuid }
        val windowWeak1Reuse = DI.windowsMappedModule()!!.windowWeak("1").get()!!.map { it.uuid }
        val windowSoft1Reuse = DI.windowsMappedModule()!!.windowSoft("1").get()!!.map { it.uuid }
        val windowStrong1Reuse = DI.windowsMappedModule()!!.windowStrong("1").get()!!.map { it.uuid }
        val windowsFactory2Reuse = DI.windowsMappedModule()!!.windowFactory("2").get()!!.map { it.uuid }
        val windowWeak2Reuse = DI.windowsMappedModule()!!.windowWeak("2").get()!!.map { it.uuid }
        val windowSoft2Reuse = DI.windowsMappedModule()!!.windowSoft("2").get()!!.map { it.uuid }
        val windowStrong2Reuse = DI.windowsMappedModule()!!.windowStrong("2").get()!!.map { it.uuid }

        // Then
        assertNotEquals(windowsFactory1, windowsFactory1Reuse)
        assertNotEquals(windowsFactory2, windowsFactory2Reuse)
        assertNotEquals(windowWeak1, windowWeak1Reuse)
        assertNotEquals(windowWeak2, windowWeak2Reuse)
        assertEquals(windowSoft1, windowSoft1Reuse)
        assertEquals(windowSoft2, windowSoft2Reuse)
        assertEquals(windowStrong1, windowStrong1Reuse)
        assertEquals(windowStrong2, windowStrong2Reuse)
    }
}
