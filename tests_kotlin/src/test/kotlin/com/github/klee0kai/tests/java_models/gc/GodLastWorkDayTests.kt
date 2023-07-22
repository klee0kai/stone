package com.github.klee0kai.tests.java_models.gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.gcforest.GcGodComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Saturn
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference

class GodLastWorkDayTests {
    @Test
    fun gcAllTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcAll()

        //Then
        assertNull(sunRef.get())
        assertNull(earthRef.get())
        assertNull(saturnRef.get())
    }

    @Test
    fun gcWeakTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcWeak()

        //Then
        assertNotNull(sunRef.get())
        assertNotNull(earthRef.get())
        assertNull(saturnRef.get())
    }

    @Test
    fun gcSoftTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcSoft()

        //Then
        assertNotNull(sunRef.get())
        assertNull(earthRef.get())
        assertNull(saturnRef.get())
    }

    @Test
    fun gcStrongTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcStrong()

        //Then
        assertNull(sunRef.get())
        assertNotNull(earthRef.get())
        assertNull(saturnRef.get())
    }

    @Test
    fun gcSunTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcSun()

        //Then
        assertNull(sunRef.get())
        assertNotNull(earthRef.get())
        assertNull(saturnRef.get())
    }

    @Test
    fun gcPlanetsTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcPlanets()

        //Then
        assertNotNull(sunRef.get())
        assertNull(earthRef.get())
        assertNull(saturnRef.get())
    }

    @Test
    fun gcSunAndPlanetsTest() {
        //Given
        val di = Stone.createComponent(GcGodComponent::class.java)
        val sunRef = WeakReference(Sun())
        val earthRef = WeakReference(Earth())
        val saturnRef = WeakReference(Saturn())
        di.bind(sunRef.get())
        di.bind(earthRef.get())
        di.bind(saturnRef.get())

        //When
        di.gcSunAndPlanets()

        //Then
        assertNull(sunRef.get())
        assertNull(earthRef.get())
        assertNull(saturnRef.get())
    }
}
