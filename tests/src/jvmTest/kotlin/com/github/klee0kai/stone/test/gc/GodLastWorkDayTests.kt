package com.github.klee0kai.stone.test.gc

import com.github.klee0kai.stone.weakref.WeakRef
import com.github.klee0kai.test.di.gcforest.GcGodComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Saturn
import com.github.klee0kai.test.mowgli.galaxy.Sun
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GodLastWorkDayTests {

    @Test
    fun gcAllTest() {
        //Given
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
        val di = GcGodComponentStoneComponent()
        val sunRef = WeakRef(Sun())
        val earthRef = WeakRef(Earth())
        val saturnRef = WeakRef(Saturn())
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
