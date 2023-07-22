package com.github.klee0kai.stone.kotlin.test.car.cachecontrol.swcache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.stone.kotlin.test.utils.KotlinUtils.resetKotlinRegisters
import com.github.klee0kai.test.car.di.cachecontrol.swcache.CarSwCacheComponent
import com.github.klee0kai.test.car.model.Bumper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class BumperSwitchCacheTests {
    @Test
    fun allWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.allWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(0, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun weakToStrongTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.weakToStrongFewMillis()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(3, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongAfterFewMillisTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.weakToStrongFewMillis()
        Thread.sleep(150)
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun softToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.softToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(0, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun strongToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.strongToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun bumpersToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.bumpersToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(0, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun redBumpersToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.redBumpersToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun redBumpersToWeak2Test() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.redBumpersToWeak2()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(0, nonNullCount(bumperStrong))
    }

    @Test
    fun wheelsToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }


        //When
        resetKotlinRegisters()
        DI.wheelsToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    @Test
    fun nothingToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val bumperFactory = DI.bumpersModule()!!.bumperFactory().map { WeakReference(it) }
        val bumperWeak = DI.bumpersModule()!!.bumperWeak().map { WeakReference(it) }
        val bumperSoft = DI.bumpersModule()!!.bumperSoft().map { WeakReference(it) }
        val bumperStrong = DI.bumpersModule()!!.bumperStrong().map { WeakReference(it) }

        //When
        resetKotlinRegisters()
        DI.nothingToWeak()
        System.gc()

        // Then
        assertEquals(0, nonNullCount(bumperFactory))
        assertEquals(0, nonNullCount(bumperWeak))
        assertEquals(3, nonNullCount(bumperSoft))
        assertEquals(3, nonNullCount(bumperStrong))
    }

    private fun nonNullCount(list: List<WeakReference<Bumper?>?>): Int = list.count { it?.get() != null }
}
