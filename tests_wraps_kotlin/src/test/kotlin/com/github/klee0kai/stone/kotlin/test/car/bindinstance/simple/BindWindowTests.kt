package com.github.klee0kai.stone.kotlin.test.car.bindinstance.simple

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class BindWindowTests {
    @Test
    fun noBindTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //Then
        assertNull(DI.module().windows())
        assertNull(DI.provideWindow())
        assertEquals(0, DI.provideWindows()!!.size)
    }

    @Test
    fun nullBindTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //When
        DI.bindWindow(null)

        //Then
        assertNull(DI.module().windows())
        assertNull(DI.provideWindow())
        assertEquals(0, DI.provideWindows()!!.size)
    }

    @Test
    fun bindWindowSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //When
        val window = Window()
        DI.bindWindow(window)

        //Then
        assertEquals(window.uuid, DI.module().windows()!!.get()!![0]!!.uuid)
        assertEquals(window.uuid, DI.provideWindow().uuid)
        assertEquals(1, DI.provideWindows()!!.size)
        assertEquals(window.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
    }

    @Test
    fun rebindWindowSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)
        DI.bindWindow(Window())

        //When
        val window = Window()
        DI.bindWindow(window)

        //Then
        assertEquals(window.uuid, DI.module().windows()!!.get()!![0]!!.uuid)
        assertEquals(window.uuid, DI.provideWindow().uuid)
        assertEquals(1, DI.provideWindows()!!.size)
        assertEquals(window.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
    }
}
