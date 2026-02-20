package com.github.klee0kai.stone.test.bindinstance.singlemethod_inject

import com.github.klee0kai.stone.weakref.Memory
import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponentStoneComponent
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import com.github.klee0kai.test.mowgli.galaxy.Sun
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MoonSkyProtectInjectedTests {

    @Test
    fun withoutProtectInjectedTest() {
        //Given
        val component = StarSkyComponentStoneComponent()
        val mercury = WeakRef(Mercury())
        val star = WeakRef(Sun())
        component.starModule().star(star.get())
        component.mercury(mercury.get())

        //When
        var moonSky: MoonSky? = MoonSky()
        component.inject(moonSky)
        moonSky = null
        component.gcAll()


        //Then
        assertNull(mercury.get())
        assertNull(star.get())
    }


    @Test
    fun withProtectInjectedTest() {
        //Given
        Memory.gc()
        val component = StarSkyComponentStoneComponent()
        val mercury = WeakRef(Mercury())
        val star = WeakRef(Sun())
        component.starModule().star(star.get())
        component.mercury(mercury.get())

        //When
        var moonSky: MoonSky? = MoonSky()
        component.inject(moonSky)
        component.protectInjected(moonSky)
        moonSky = null
        component.gcAll()

        assertNotNull(mercury.get())
        assertNotNull(star.get())

        //Then after protect finished
//        Thread.sleep(100)
        Memory.gc()
        assertNull(mercury.get())
        assertNull(star.get())
    }

}
