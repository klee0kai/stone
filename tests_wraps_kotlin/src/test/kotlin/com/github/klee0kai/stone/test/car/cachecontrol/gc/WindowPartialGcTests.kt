package com.github.klee0kai.stone.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class WindowPartialGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //Then
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun holdInListTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }
        val holder = DI.windowsModule()!!.windowStrong().get()!![1]


        //When
        DI.gcAll()

        // Then
        assertNotNull(holder)
        assertEquals(1, nonNullCount(windowStrong))
        assertNotNull(windowStrong[1]!!.get())
    }

    @Test
    fun partialRecreateList1Test() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val uids1 = DI.windowsModule()!!.windowStrong().get()!!.map { it.uuid }
        val holder = DI.windowsModule()!!.windowStrong().get()!![0]


        //When
        DI.gcAll()
        val uids2 = DI.windowsModule()!!.windowStrong().get()!!.map { it.uuid }


        // Then
        assertNotNull(holder)
        assertEquals(uids1[0], uids2[0])
        assertNotEquals(uids1[1], uids2[1])
        assertNotEquals(uids1[2], uids2[2])
    }

    @Test
    fun partialRecreateList2Test() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val uids1 = DI.windowsModule()!!.windowStrong().get()!!.map { it.uuid }
        val holder = DI.windowsModule()!!.windowStrong().get()!![1]


        //When
        DI.gcAll()
        val uids2 = DI.windowsModule()!!.windowStrong().get()!!.map { it.uuid }


        // Then
        assertNotNull(holder)
        assertNotEquals(uids1[0], uids2[0])
        assertEquals(uids1[1], uids2[1])
        assertNotEquals(uids1[2], uids2[2])
    }

    @Test
    fun partialRecreateList3Test() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val uids1 = DI.windowsModule()!!.windowStrong().get()!!.map { it.uuid }
        val holder = DI.windowsModule()!!.windowStrong().get()!![2]


        //When
        DI.gcAll()
        val uids2 = DI.windowsModule()!!.windowStrong().get()!!.map { it.uuid }


        // Then
        assertNotNull(holder)
        assertNotEquals(uids1[0], uids2[0])
        assertNotEquals(uids1[1], uids2[1])
        assertEquals(uids1[2], uids2[2])
    }

    private fun nonNullCount(list: List<WeakReference<Window?>?>): Int = list.count { it?.get() != null }
}
