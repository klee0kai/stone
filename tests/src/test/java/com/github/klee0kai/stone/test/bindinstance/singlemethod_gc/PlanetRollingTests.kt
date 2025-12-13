package com.github.klee0kai.stone.test.bindinstance.singlemethod_gc

import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class PlanetRollingTests {

    @Test
    fun gcAllTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())

        //When
        DI.gcAll()

        //Then
        for (ref in listOf(
            earthStrong, earthSoft, planetWeak,
            sunStrong, sunSoft, starWeak
        )) {
            assertNull(ref.get())
        }
    }


    @Test
    fun gcStrongTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())

        //When
        DI.gcStrong()

        //Then
        for (ref in listOf(
            earthStrong, planetWeak,
            sunStrong, starWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthSoft,
            sunSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())

        //When
        DI.gcSoft()

        //Then
        for (ref in listOf(
            earthSoft, planetWeak,
            sunSoft, starWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong,
            sunStrong
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcWeakTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())

        //When
        DI.gcWeak()

        //Then
        for (ref in listOf(
            planetWeak,
            starWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong, earthSoft,
            sunStrong, sunSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftSunTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())

        //When
        DI.gcSoftSun()

        //Then
        for (ref in listOf(
            planetWeak,
            sunSoft, starWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong, earthSoft,
            sunStrong
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftPlanetsTest() {
        //Given
        val DI = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())

        //When
        DI.gcSoftPlanets()

        //Then
        for (ref in listOf(
            earthSoft, planetWeak,
            starWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong,
            sunStrong, sunSoft
        )) {
            assertNotNull(ref.get())
        }
    }
}
