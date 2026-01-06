package com.github.klee0kai.stone.kotlin.test.car.bindinstance.simple

import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponentStoneComponent
import com.github.klee0kai.test.car.model.Wheel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class BindWheelRefTests {

    @Test
    fun noBindTest() {
        //Given
        val DI = CarBindComponentStoneComponent()

        //Then
        assertNull(DI.module().wheel())
        assertNull(DI.provideWheel())
        assertNull(DI.provideWheelRef())
        assertEquals(0, DI.provideWheels()!!.size)
    }

    @Test
    fun nullBindTest() {
        //Given
        val DI = CarBindComponentStoneComponent()

        //When
        DI.bindWheelRef(null)

        //Then
        assertNull(DI.module()!!.wheel())
        assertNull(DI.provideWheel())
        assertNull(DI.provideWheelRef())
        assertEquals(0, DI.provideWheels()!!.size)
    }

    @Test
    fun nullRefBindTest() {
        //Given
        val DI = CarBindComponentStoneComponent()

        //When
        DI.bindWheelRef(WeakReference(null))

        //Then
        assertNull(DI.module().wheel())
        assertNull(DI.provideWheel())
        assertNull(DI.provideWheelRef())
        assertEquals(0, DI.provideWheels()!!.size)
    }

    @Test
    fun bindWheelSimpleTest() {
        //Given
        val DI = CarBindComponentStoneComponent()

        //When
        val wheel = Wheel()
        DI.bindWheelRef(WeakReference(wheel))

        //Then
        assertEquals(wheel.uuid, DI.module()!!.wheel()!!.uuid)
        assertEquals(wheel.uuid, DI.provideWheel()!!.uuid)
        assertEquals(wheel.uuid, DI.provideWheelRef()!!.get()!!.uuid)
        assertEquals(1, DI.provideWheels()!!.size)
        assertEquals(wheel.uuid, DI.provideWheels()!![0]!!.get()!!.uuid)
    }

    @Test
    fun rebindWheelSimpleTest() {
        //Given
        val DI = CarBindComponentStoneComponent()
        DI.bindWheel(Wheel())

        //When
        val wheel = Wheel()
        DI.bindWheelRef(WeakReference(wheel))

        //Then
        assertEquals(wheel.uuid, DI.module()!!.wheel()!!.uuid)
        assertEquals(wheel.uuid, DI.provideWheel()!!.uuid)
        assertEquals(wheel.uuid, DI.provideWheelRef()!!.get()!!.uuid)
        assertEquals(1, DI.provideWheels()!!.size)
        assertEquals(wheel.uuid, DI.provideWheels()!![0]!!.get()!!.uuid)
    }
}
