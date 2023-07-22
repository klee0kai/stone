package com.github.klee0kai.stone.test.car.cachecontrol.swcache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.car.di.cachecontrol.swcache.CarSwCacheComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class WheelSwitchCacheTests {
    @Test
    fun allWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.allWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }

    @Test
    fun weakToStrongTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.weakToStrongFewMillis()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNotNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongAfterFewMillisTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.weakToStrongFewMillis()
        Thread.sleep(150)
        System.gc()


        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun weakToSoftTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.weakToSoftFewMillis()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNotNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToSoftAfterFewMillisTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.weakToSoftFewMillis()
        Thread.sleep(150)
        System.gc()


        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun softToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.softToWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun strongToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.strongToWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }

    @Test
    fun wheelsToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.wheelsToWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }

    @Test
    fun nothinToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.nothingToWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun windowsToWeakTest() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.windowsToWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNotNull(wheelSoft!!.get())
        assertNotNull(wheelStrong!!.get())
    }

    @Test
    fun windowAndWheelsToWeak() {
        // Given
        val DI = Stone.createComponent(CarSwCacheComponent::class.java)
        val wheelFactory = DI.wheelsModule()!!.wheelFactory()
        val wheelWeak = DI.wheelsModule()!!.wheelWeak()
        val wheelSoft = DI.wheelsModule()!!.wheelSoft()
        val wheelStrong = DI.wheelsModule()!!.wheelStrong()

        //When
        DI.windowsAndWheelsToWeak()
        System.gc()

        // Then
        assertNull(wheelFactory!!.get())
        assertNull(wheelWeak!!.get())
        assertNull(wheelSoft!!.get())
        assertNull(wheelStrong!!.get())
    }
}
