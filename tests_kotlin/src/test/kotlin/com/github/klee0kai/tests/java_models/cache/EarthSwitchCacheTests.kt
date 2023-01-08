package com.github.klee0kai.tests.java_models.cache

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.swcache.SwitchCacheComponent
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class EarthSwitchCacheTests {
    @Test
    fun allToWeakTest() {
        //Given
        val di = Stone.createComponent(SwitchCacheComponent::class.java)
        val mountain = WeakReference(di.earth().mountainStrong())

        //When
        di.allWeak()
        System.gc()

        //Then
        assertNull(mountain.get())
    }

    @Test
    fun strongToWeakTest() {
        //Given
        val di = Stone.createComponent(SwitchCacheComponent::class.java)
        val mountainStrong = WeakReference(di.earth().mountainStrong())
        val mountainSoft = WeakReference(di.earth().mountainSoft())

        //When
        di.strongToWeak()
        System.gc()

        //Then
        assertNull(mountainStrong.get())
        assertNotNull(mountainSoft.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongFewMillisTest() {
        //Given
        val di = Stone.createComponent(SwitchCacheComponent::class.java)
        val mountainWeak = WeakReference(di.earth().mountainWeak())

        //When
        di.allStrongFewMillis()
        System.gc()

        //Then: can't GC
        assertNotNull(mountainWeak.get())

        //When: after few millis
        Thread.sleep(110)
        System.gc()

        //Then: can GC
        assertNull(mountainWeak.get())
    }

    @Test
    fun mountainToWeakTest() {
        //Given
        val di = Stone.createComponent(SwitchCacheComponent::class.java)
        val mountain = WeakReference(di.earth().mountainStrong())
        val river = WeakReference(di.earth().riverSoft())

        //When
        di.mountainToWeak()
        System.gc()

        //Then
        assertNull(mountain.get())
        assertNotNull(river.get())
    }
}