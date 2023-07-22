package com.github.klee0kai.stone.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class WheelReusableGcTests {
    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule().wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule().wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule().wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule().wheelStrong()!!.get()!!.uuid

        //When
        DI.gcAll()
        val wheelFactory2 = DI.wheelsModule().wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule().wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule().wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule().wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertNotEquals(wheelSoft1, wheelSoft2)
        assertNotEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcWeak()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertEquals(wheelSoft1, wheelSoft2)
        assertEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcSoft()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertNotEquals(wheelSoft1, wheelSoft2)
        assertEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcStrong()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertEquals(wheelSoft1, wheelSoft2)
        assertNotEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcWheels()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertNotEquals(wheelSoft1, wheelSoft2)
        assertNotEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcNothing()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertEquals(wheelSoft1, wheelSoft2)
        assertEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcWindows() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcWindows()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertEquals(wheelSoft1, wheelSoft2)
        assertEquals(wheelStrong1, wheelStrong2)
    }

    @Test
    fun gcWindowsAndWheels() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory1 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak1 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft1 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong1 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        //When
        DI.gcWindowsAndWheels()
        val wheelFactory2 = DI.wheelsModule()!!.wheelFactory()!!.get()!!.uuid
        val wheelWeak2 = DI.wheelsModule()!!.wheelWeak()!!.get()!!.uuid
        val wheelSoft2 = DI.wheelsModule()!!.wheelSoft()!!.get()!!.uuid
        val wheelStrong2 = DI.wheelsModule()!!.wheelStrong()!!.get()!!.uuid

        // Then
        assertNotEquals(wheelFactory1, wheelFactory2)
        assertNotEquals(wheelWeak1, wheelWeak2)
        assertNotEquals(wheelSoft1, wheelSoft2)
        assertNotEquals(wheelStrong1, wheelStrong2)
    }
}
