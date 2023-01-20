package com.github.klee0kai.tests.java_models.bindinstance.singlemethod_inject

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponent
import com.github.klee0kai.test.mowgli.MoonSky
import com.github.klee0kai.test.mowgli.galaxy.Mercury
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class MoonSkyPotectInjectedTests {
    @Test
    fun withoutProtectInjectedTest() {
        //Given
        val component = Stone.createComponent(StarSkyComponent::class.java)
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
        val component = Stone.createComponent(StarSkyComponent::class.java)
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
        Thread.sleep(50)
        System.gc()
        assertNull(mercury.get())
        assertNull(star.get())
    }
}