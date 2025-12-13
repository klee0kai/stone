package com.github.klee0kai.stone.test.bindinstance.singlemethod_inject

import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponentStoneComponent
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class MoonSkyProtectInjectedTests {

    @Test
    fun withoutProtectInjectedTest() {
        //Given
        val component = StarSkyComponentStoneComponent()
        val mercury = WeakReference(Mercury())
        val star = WeakReference(Sun())
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
    @Throws(InterruptedException::class)
    fun withProtectInjectedTest() {
        //Given
        System.gc()
        val component = StarSkyComponentStoneComponent()
        val mercury = WeakReference(Mercury())
        val star = WeakReference(Sun())
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
        Thread.sleep(100)
        System.gc()
        assertNull(mercury.get())
        assertNull(star.get())
    }
}
