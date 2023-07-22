package com.github.klee0kai.stone.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class WheelMultiMappedGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //Then
        assertNotNull(wheelFactory.get())
        assertNotNull(wheelWeak.get())
        assertNotNull(wheelSoft.get())
        assertNotNull(wheelStrong.get())
    }

    @Test
    fun createAfterGcWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        DI.gcAll()

        //When
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //Then
        assertNotNull(wheelFactory.get())
        assertNotNull(wheelWeak.get())
        assertNotNull(wheelSoft.get())
        assertNotNull(wheelStrong.get())
    }

    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcAll()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNull(wheelSoft.get())
        assertNull(wheelStrong.get())
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcWeak()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNotNull(wheelSoft.get())
        assertNotNull(wheelStrong.get())
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcSoft()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNull(wheelSoft.get())
        assertNotNull(wheelStrong.get())
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcStrong()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNotNull(wheelSoft.get())
        assertNull(wheelStrong.get())
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcWheels()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNull(wheelSoft.get())
        assertNull(wheelStrong.get())
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcNothing()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNotNull(wheelSoft.get())
        assertNotNull(wheelStrong.get())
    }

    @Test
    fun gcWindows() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcWindows()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNotNull(wheelSoft.get())
        assertNotNull(wheelStrong.get())
    }

    @Test
    fun gcWindowsAndWheels() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val wheelFactory = DI.wheelMultiMappedModule().wheelFactory("1", Integer(1))
        val wheelWeak = DI.wheelMultiMappedModule().wheelWeak("1", Integer(1))
        val wheelSoft = DI.wheelMultiMappedModule().wheelSoft("1", Integer(1))
        val wheelStrong = DI.wheelMultiMappedModule().wheelStrong("1", Integer(1))

        //When
        DI.gcWindowsAndWheels()

        // Then
        assertNull(wheelFactory.get())
        assertNull(wheelWeak.get())
        assertNull(wheelSoft.get())
        assertNull(wheelStrong.get())
    }
}
