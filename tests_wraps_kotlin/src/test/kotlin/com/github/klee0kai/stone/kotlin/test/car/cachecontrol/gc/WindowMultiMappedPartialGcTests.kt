package com.github.klee0kai.stone.kotlin.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.kotlin.test.utils.KotlinUtils.resetKotlinRegisters
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class WindowMultiMappedPartialGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val windowStrong1 = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong2 = DI.windowsMultiMappedModule()!!.windowStrong(1, "2").get()!!.map { WeakReference(it) }

        //Then
        assertEquals(3, nonNullCount(windowStrong1))
        assertEquals(3, nonNullCount(windowStrong2))
    }

    @Test
    fun holdInListTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowStrong1 = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong2 = DI.windowsMultiMappedModule()!!.windowStrong(2, "2").get()!!.map { WeakReference(it) }
        val holder1 = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!![1]
        val holder2 = DI.windowsMultiMappedModule()!!.windowStrong(2, "2").get()!![0]


        //When
        resetKotlinRegisters()
        DI.gcAll()

        // Then
        assertNotNull(holder1)
        assertNotNull(holder2)
        assertEquals(1, nonNullCount(windowStrong1))
        assertEquals(1, nonNullCount(windowStrong2))
        assertNotNull(windowStrong1[1].get())
        assertNotNull(windowStrong2[0].get())
    }

    @Test
    fun partialRecreateList1Test() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val uids1 = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { it.uuid }
        val uids2 = DI.windowsMultiMappedModule()!!.windowStrong(1, "2").get()!!.map { it.uuid }
        val uids3 = DI.windowsMultiMappedModule()!!.windowStrong(2, "1").get()!!.map { it.uuid }
        val holder1 = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!![0]
        val holder2 = DI.windowsMultiMappedModule()!!.windowStrong(1, "2").get()!![1]
        val holder3 = DI.windowsMultiMappedModule()!!.windowStrong(2, "1").get()!![2]


        //When
        resetKotlinRegisters()
        DI.gcAll()
        val uids1Reused = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { it.uuid }
        val uids2Reused = DI.windowsMultiMappedModule()!!.windowStrong(1, "2").get()!!.map { it.uuid }
        val uids3Reused = DI.windowsMultiMappedModule()!!.windowStrong(2, "1").get()!!.map { it.uuid }


        // Then
        assertNotNull(holder1)
        assertNotNull(holder2)
        assertNotNull(holder3)
        assertEquals(uids1[0], uids1Reused[0])
        assertNotEquals(uids1[1], uids1Reused[1])
        assertNotEquals(uids1[2], uids1Reused[2])
        assertNotEquals(uids2[0], uids2Reused[0])
        assertEquals(uids2[1], uids2Reused[1])
        assertNotEquals(uids2[2], uids2Reused[2])
        assertNotEquals(uids3[0], uids3Reused[0])
        assertNotEquals(uids3[1], uids3Reused[1])
        assertEquals(uids3[2], uids3Reused[2])
    }

    private fun nonNullCount(list: List<WeakReference<Window?>?>): Int = list.count { it?.get() != null }
}
