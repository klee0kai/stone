package com.github.klee0kai.tests.java_models.bindinstance.singlemethod_gc

import com.github.klee0kai.stone.Stone
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Sun
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.lang.ref.WeakReference
import java.util.*

class PlanetRollingTests {
    @Test
    fun gcAllTest() {
        //Given
        val DI = Stone.createComponent(PlanetRollingComponent::class.java)
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
        for (ref in Arrays.asList(
            earthStrong, earthSoft, planetWeak,
            sunStrong, sunSoft, starWeak
        )) {
            assertNull(ref.get())
        }
    }

    @Test
    fun gcStrongTest() {
        //Given
        val DI = Stone.createComponent(PlanetRollingComponent::class.java)
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
        for (ref in Arrays.asList(
            earthStrong, planetWeak,
            sunStrong, starWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            earthSoft,
            sunSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftTest() {
        //Given
        val DI = Stone.createComponent(PlanetRollingComponent::class.java)
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
        for (ref in Arrays.asList(
            earthSoft, planetWeak,
            sunSoft, starWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            earthStrong,
            sunStrong
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcWeakTest() {
        //Given
        val DI = Stone.createComponent(PlanetRollingComponent::class.java)
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
        for (ref in Arrays.asList(
            planetWeak,
            starWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            earthStrong, earthSoft,
            sunStrong, sunSoft
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftSunTest() {
        //Given
        val DI = Stone.createComponent(PlanetRollingComponent::class.java)
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
        for (ref in Arrays.asList(
            planetWeak,
            sunSoft, starWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            earthStrong, earthSoft,
            sunStrong
        )) {
            assertNotNull(ref.get())
        }
    }

    @Test
    fun gcSoftPlanetsTest() {
        //Given
        val DI = Stone.createComponent(PlanetRollingComponent::class.java)
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
        for (ref in Arrays.asList(
            earthSoft, planetWeak,
            starWeak
        )) {
            assertNull(ref.get())
        }
        for (ref in Arrays.asList(
            earthStrong,
            sunStrong, sunSoft
        )) {
            assertNotNull(ref.get())
        }
    }
}