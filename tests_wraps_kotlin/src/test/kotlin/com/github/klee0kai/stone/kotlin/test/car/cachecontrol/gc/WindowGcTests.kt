package com.github.klee0kai.stone.kotlin.test.car.cachecontrol.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.kotlin.test.utils.KotlinUtils.resetKotlinRegisters
import com.github.klee0kai.test.car.di.cachecontrol.gc.CarGcComponent
import com.github.klee0kai.test.car.model.Window
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class WindowGcTests {
    @Test
    fun createWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)

        //When
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //Then
        assertEquals(3, nonNullCount(windowsFactory))
        assertEquals(3, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun createAfterGcWorkCorrect() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        DI.gcAll()

        //When
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //Then
        assertEquals(3, nonNullCount(windowsFactory))
        assertEquals(3, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun gcAllTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }


        //When
        resetKotlinRegisters()
        DI.gcAll()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun gcWeakTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.gcWeak()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun gcSoftTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.gcSoft()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun gcStrongTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.gcStrong()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun gcWindows() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.gcWindows()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun gcWindowsAndWheels() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.gcWindowsAndWheels()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(0, nonNullCount(windowSoft))
        assertEquals(0, nonNullCount(windowStrong))
    }

    @Test
    fun gcWheelsTest() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }


        //When
        resetKotlinRegisters()
        DI.gcWheels()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    @Test
    fun gcNothing() {
        // Given
        val DI = Stone.createComponent(CarGcComponent::class.java)
        val windowsFactory = DI.windowsModule()!!.windowFactory().get()!!.map { WeakReference(it) }
        val windowWeak = DI.windowsModule()!!.windowWeak().get()!!.map { WeakReference(it) }
        val windowSoft = DI.windowsModule()!!.windowSoft().get()!!.map { WeakReference(it) }
        val windowStrong = DI.windowsModule()!!.windowStrong().get()!!.map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.gcNothing()

        // Then
        assertEquals(0, nonNullCount(windowsFactory))
        assertEquals(0, nonNullCount(windowWeak))
        assertEquals(3, nonNullCount(windowSoft))
        assertEquals(3, nonNullCount(windowStrong))
    }

    private fun nonNullCount(list: List<WeakReference<Window>?>): Int =
        list.count { it?.get() != null }

}
