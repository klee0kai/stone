package com.github.klee0kai.stone.kotlin.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class WheelGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //Then
        assertNotNull(wheelFactory!!.get())
        assertNotNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun createAfterGcWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        DI.gcAll()

        //When
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //Then
        assertNotNull(wheelFactory!!.get())
        assertNotNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcAll()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcWeak()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcSoft()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcStrong()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcWheels()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcNothing()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun gcWindows() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcWindows()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun gcWindowsAndWheels() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelsModule().wheelFactory()
        val wheelWeak = DI.wheelsModule().wheelWeak()
        val wheelSoft = DI.wheelsModule().wheelSoft()
        val wheelStrong = DI.wheelsModule().wheelStrong()

        //When
        DI.gcWindowsAndWheels()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }
}
