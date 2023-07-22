package com.github.klee0kai.stone.test.car.bindinstance.simple

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.bindinstance.simple.CarBindComponent
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BindWindowCollectionTests {
    @Test
    fun bindWindowSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //When
        val window = Window()
        DI.bindWindows(listOf(window))

        //Then
        assertEquals(window.uuid, DI.module().windows()!!.get()!![0]!!.uuid)
        assertEquals(window.uuid, DI.provideWindow().uuid)
        assertEquals(1, DI.provideWindows()!!.size)
        assertEquals(window.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
    }

    @Test
    fun bindWindowListTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)

        //When
        val window1 = Window()
        val window2 = Window()
        DI.bindWindows(listOf(window1, window2))

        //Then
        assertEquals(window1.uuid, DI.module()!!.windows()!!.get()!![0]!!.uuid)
        assertEquals(window2.uuid, DI.module()!!.windows()!!.get()!![1]!!.uuid)
        assertEquals(window1.uuid, DI.provideWindow()!!.uuid)
        assertEquals(2, DI.provideWindows()!!.size)
        assertEquals(window1.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
        assertEquals(window2.uuid, ArrayList(DI.provideWindows())[1]!!.uuid)
    }

    @Test
    fun rebindWindowSimpleTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)
        DI.bindWindow(Window())

        //When
        val window = Window()
        DI.bindWindows(listOf(window))

        //Then
        assertEquals(window.uuid, DI.module()!!.windows()!!.get()!![0]!!.uuid)
        assertEquals(window.uuid, DI.provideWindow()!!.uuid)
        assertEquals(1, DI.provideWindows()!!.size)
        assertEquals(window.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
    }

    @Test
    fun rebindWindowListTest() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)
        DI.bindWindow(Window())

        //When
        val window1 = Window()
        val window2 = Window()
        DI.bindWindows(listOf(window1, window2))

        //Then
        assertEquals(window1.uuid, DI.module()!!.windows()!!.get()!![0]!!.uuid)
        assertEquals(window2.uuid, DI.module()!!.windows()!!.get()!![1]!!.uuid)
        assertEquals(window1.uuid, DI.provideWindow()!!.uuid)
        assertEquals(2, DI.provideWindows()!!.size)
        assertEquals(window1.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
        assertEquals(window2.uuid, ArrayList(DI.provideWindows())[1]!!.uuid)
    }

    @Test
    fun rebindWindowSimple2Test() {
        //Given
        val DI = Stone.createComponent(CarBindComponent::class.java)
        DI.bindWindows(listOf(Window()))

        //When
        val window = Window()
        DI.bindWindows(listOf(window))

        //Then
        assertEquals(window.uuid, DI.module()!!.windows()!!.get()!![0]!!.uuid)
        assertEquals(window.uuid, DI.provideWindow()!!.uuid)
        assertEquals(1, DI.provideWindows()!!.size)
        assertEquals(window.uuid, ArrayList(DI.provideWindows())[0]!!.uuid)
    }
}
