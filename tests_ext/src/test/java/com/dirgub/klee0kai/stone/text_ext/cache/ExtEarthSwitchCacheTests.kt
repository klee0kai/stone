package com.dirgub.klee0kai.stone.text_ext.cache

import com.github.klee0kai.test.di.swcache.SwitchCacheComponent
import com.github.klee0kai.test.di.swcache.SwitchCacheComponentStoneComponent
import com.github.klee0kai.test.mowgli.earth.Mountain
import com.github.klee0kai.test_ext.inject.di.swcache.SwitchCacheExtComponent
import com.github.klee0kai.test_ext.inject.di.swcache.SwitchCacheExtComponentStoneComponent
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class ExtEarthSwitchCacheTests {

    @Test
    fun allToWeakTest() {
        //Given
        val DI: SwitchCacheComponent = SwitchCacheComponentStoneComponent()
        val DIPro: SwitchCacheExtComponent = SwitchCacheExtComponentStoneComponent()
        DIPro.extOf(DI)
        val mountain = WeakReference<Mountain?>(DIPro.earth()!!.mountainStrong())

        //When
        DIPro.allWeakExt()
        System.gc()

        //Then
        org.junit.jupiter.api.Assertions.assertNull(mountain.get())
    }

    @Test
    fun strongToWeakTest() {
        //Given
        val DI: SwitchCacheComponent = SwitchCacheComponentStoneComponent()
        val DIPro: SwitchCacheExtComponent = SwitchCacheExtComponentStoneComponent()
        DIPro.extOf(DI)
        val mountainStrong = WeakReference<Mountain?>(DIPro.earth()!!.mountainStrong())
        val mountainSoft = WeakReference<Mountain?>(DIPro.earth()!!.mountainSoft())

        //When
        DIPro.strongToWeakExt()
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
        val mountainWeak = WeakReference<Mountain?>(DIPro.earth()!!.mountainWeak())

        //When
        DIPro.allStrongFewMillisExt()
        System.gc()

        //Then: can't GC
        assertNotNull(mountainWeak.get())

        //When: after few millis
        Thread.sleep(110)
        System.gc()

        //Then: can GC
        assertNull(mountainWeak.get())
    }
}
