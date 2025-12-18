package com.dirgub.klee0kai.stone.text_ext.cache

import com.github.klee0kai.test.di.swcache.SwitchCacheComponent
import com.github.klee0kai.test.di.swcache.SwitchCacheComponentStoneComponent
import com.github.klee0kai.test.mowgli.earth.Mountain
import com.github.klee0kai.test.mowgli.earth.River
import com.github.klee0kai.test_ext.inject.di.swcache.SwitchCacheExtComponent
import com.github.klee0kai.test_ext.inject.di.swcache.SwitchCacheExtComponentStoneComponent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class EarthSwitchCacheFromProtoTests {

    @Test
    fun allToWeakTest() {
        //Given
        val DI: SwitchCacheComponent = SwitchCacheComponentStoneComponent()
        val DIPro: SwitchCacheExtComponent = SwitchCacheExtComponentStoneComponent()
        DIPro.extOf(DI)
        val mountain = WeakReference<Mountain?>(DI.earth()!!.mountainStrong())

        //When
        DIPro.allWeak()
        System.gc()

        //Then
        assertNull(mountain.get())
    }

    @Test
    fun strongToWeakTest() {
        //Given
        val DI: SwitchCacheComponent = SwitchCacheComponentStoneComponent()
        val DIPro: SwitchCacheExtComponent = SwitchCacheExtComponentStoneComponent()
        DIPro.extOf(DI)
        val mountainStrong = WeakReference<Mountain?>(DI.earth()!!.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DI.earth()!!.mountainSoft())

        //When
        DIPro.strongToWeak()
        System.gc()

        //Then
        assertNull(mountainStrong.get())
        assertNotNull(mountainSoft.get())
    }

    @Test
    @Throws(InterruptedException::class)
    fun weakToStrongFewMillisTest() {
        //Given
        val DI: SwitchCacheComponent = SwitchCacheComponentStoneComponent()
        val DIPro: SwitchCacheExtComponent = SwitchCacheExtComponentStoneComponent()
        DIPro.extOf(DI)
        val mountainWeak = WeakReference<Mountain?>(DI.earth()!!.mountainWeak())

        //When
        DIPro.allStrongFewMillis()
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
        val DI: SwitchCacheComponent = SwitchCacheComponentStoneComponent()
        val DIPro: SwitchCacheExtComponent = SwitchCacheExtComponentStoneComponent()
        DIPro.extOf(DI)
        val mountain = WeakReference<Mountain?>(DI.earth()!!.mountainStrong())
        val river = WeakReference<River?>(DI.earth()!!.riverSoft())

        //When
        DIPro.mountainToWeak()
        System.gc()

        //Then
        assertNull(mountain.get())
        assertNotNull(river.get())
    }

}
