package com.github.klee0kai.stone.test.cache

import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.swcache.SwitchCacheComponentStoneComponent
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class EarthSwitchCacheGcTests {


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
        Thread.sleep(110)
        Memory.gc()

        //Then: can GC
        assertNull(mountainWeak.get())
    }


}
