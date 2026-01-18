package com.github.klee0kai.stone.test.cache

import com.github.klee0kai.test.di.swcache.SwitchCacheComponentStoneComponent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class EarthSwitchCacheTests {

    @Test
    fun allToWeakTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountain = WeakReference(DI.earth().mountainStrong())

        //When
        DI.allWeak()
        System.gc()

        //Then
        assertNull(mountain.get())
    }

    @Test
    fun strongToWeakTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountainStrong = WeakReference(DI.earth().mountainStrong())
        val mountainSoft = WeakReference(DI.earth().mountainSoft())

        //When
        DI.strongToWeak()
        System.gc()

        //Then
        assertNull(mountainStrong.get())
        assertNotNull(mountainSoft.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongFewMillisTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountainWeak = WeakReference(DI.earth().mountainWeak())

        //When
        DI.allStrongFewMillis()
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
        val DI = SwitchCacheComponentStoneComponent()
        val mountain = WeakReference(DI.earth().mountainStrong())
        val river = WeakReference(DI.earth().riverSoft())

        //When
        DI.mountainToWeak()
        System.gc()

        //Then
        assertNull(mountain.get())
        assertNotNull(river.get())
    }

}
