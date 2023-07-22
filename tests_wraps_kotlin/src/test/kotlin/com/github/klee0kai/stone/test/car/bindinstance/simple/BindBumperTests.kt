package com.github.klee0kai.stone.test.car.bindinstance.simple

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent
import com.github.klee0kai.test.car.model.Bumper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BindBumperTests {
    @Test
    fun noBindTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //Then
        assertNull(DI.module().bumper())
        assertNull(DI.provideBumper()!!.get())
        assertEquals(0, DI.provideBumpers()!!.size)
    }

    @Test
    fun nullBindTest() {
        //Given
        val DI = Stone.createComponent(
            CarBindComponent::class.java
        )

        //When
        DI.bindBumper(null)

        //Then
        assertNull(DI.module()!!.bumper())
        assertNull(DI.provideBumper()!!.get())
        assertEquals(0, DI.provideBumpers()!!.size)
    }

    @Test
    fun bindWheelSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //When
        val bumper = Bumper()
        DI.bindBumper { bumper }

        //Then
        assertEquals(bumper.uuid, DI.module()!!.bumper()!!.get()!!.uuid)
        assertEquals(bumper.uuid, DI.provideBumper()!!.get()!!.uuid)
        assertEquals(1, DI.provideBumpers()!!.size)
        assertEquals(bumper.uuid, DI.provideBumpers()!![0]!!.uuid)
    }

    @Test
    fun rebindWheelSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)
        DI.bindBumper { Bumper() }

        //When
        val bumper = Bumper()
        DI.bindBumper { bumper }

        //Then
        assertEquals(bumper.uuid, DI.module()!!.bumper()!!.get()!!.uuid)
        assertEquals(bumper.uuid, DI.provideBumper()!!.get()!!.uuid)
        assertEquals(1, DI.provideBumpers()!!.size)
        assertEquals(bumper.uuid, DI.provideBumpers()!![0]!!.uuid)
    }
}
