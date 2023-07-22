package com.github.klee0kai.stone.kotlin.test.car.cachecontrol.swcache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.kotlin.test.utils.KotlinUtils.resetKotlinRegisters
import com.github.klee0kai.test.car.di.cachecontrol.swcache.CarSwCacheComponent
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class WindowMultiMappedSwitchCacheTests {
    @Test
    fun allWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }


        //When
        resetKotlinRegisters()
        DI.allWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun weakToStrongTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.weakToStrongFewMillis()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(3, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongAfterFewMillisTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.weakToStrongFewMillis()
        Thread.sleep(150)
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun weakToSoftTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.weakToSoftFewMillis()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(3, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToSoftAfterFewMillisTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.weakToSoftFewMillis()
        Thread.sleep(150)
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun softToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.softToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun strongToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.strongToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun windowsToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.windowsToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun windowAndWheelsToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.windowsAndWheelsToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun wheelToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }


        //When
        resetKotlinRegisters()
        DI.wheelsToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun weakNothingTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val windowsFactory = DI.windowsMultiMappedModule()!!.windowFactory(1, "1").get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsMultiMappedModule()!!.windowWeak(1, "1").get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsMultiMappedModule()!!.windowSoft(1, "1").get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsMultiMappedModule()!!.windowStrong(1, "1").get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.nothingToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    private fun nonNullCount(list: List<WeakReference<Window?>?>): Int = list.count { it?.get() != null }
}
