package com.github.klee0kai.stone.test.cache

import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.swcache.SwitchCacheComponentStoneComponent
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EarthSwitchCacheTests {

    @Test
    fun allToWeakTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountain = WeakRef(DI.earth().mountainStrong())

        //When
        DI.allWeak()
        Memory.gc()

        //Then
        assertNull(mountain.get())
    }

    @Test
    fun strongToWeakTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountainStrong = WeakRef(DI.earth().mountainStrong())
        val mountainSoft = WeakRef(DI.earth().mountainSoft())

        //When
        DI.strongToWeak()
        Memory.gc()

        //Then
        assertNull(mountainStrong.get())
        assertNotNull(mountainSoft.get())
    }

    @Test
    fun weakToStrongFewMillisTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountainWeak = WeakRef(DI.earth().mountainWeak())

        //When
        DI.allStrongFewMillis()
        Memory.gc()

        //Then: can't GC
        assertNotNull(mountainWeak.get())

        //When: after few millis
//        Thread.sleep(110)
        Memory.gc()

        //Then: can GC
        assertNull(mountainWeak.get())
    }


    @Test
    fun mountainToWeakTest() {
        //Given
        val DI = SwitchCacheComponentStoneComponent()
        val mountain = WeakRef(DI.earth().mountainStrong())
        val river = WeakRef(DI.earth().riverSoft())

        //When
        DI.mountainToWeak()
        Memory.gc()

        //Then
        assertNull(mountain.get())
        assertNotNull(river.get())
    }

}
