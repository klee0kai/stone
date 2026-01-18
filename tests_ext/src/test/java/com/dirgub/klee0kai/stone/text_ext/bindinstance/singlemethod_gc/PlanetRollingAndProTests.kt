package com.dirgub.klee0kai.stone.text_ext.bindinstance.singlemethod_gc

import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponentStoneComponent
import com.github.klee0kai.test.mowgli.galaxy.Earth
import com.github.klee0kai.test.mowgli.galaxy.Sun
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod_gc.ExtPlanetRollingComponent
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod_gc.ExtPlanetRollingComponentStoneComponent
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.sputniks.Moon
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.stars.Sirius
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import java.lang.ref.WeakReference

class PlanetRollingAndProTests {

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
        //external component init
        val DIPro: ExtPlanetRollingComponent = ExtPlanetRollingComponentStoneComponent()
        DIPro.extOf(DI)
        val siriusStrong = WeakReference(Sirius())
        val siriusSoft = WeakReference(Sirius())
        val siriusWeak = WeakReference(Sirius())
        val moonStrong = WeakReference(Moon())
        val moonSoft = WeakReference(Moon())
        val moonWeak = WeakReference(Moon())
        DIPro.sunModule()!!.siriusStrong(siriusStrong.get())
        DIPro.sunModule()!!.siriusSoft(siriusSoft.get())
        DIPro.sunModule()!!.siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DI.gcAll()

        //Then
        for (ref in listOf(
            earthStrong, earthSoft, planetWeak,
            sunStrong, sunSoft, starWeak,
            siriusStrong, siriusSoft, siriusWeak,
            moonStrong, moonSoft, moonWeak
        )) {
            assertNull(ref.get())
        }
    }


    @Test
    fun gcStrongTest() {
        //Given
        val DI: PlanetRollingComponent = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule()!!.sunStrong(sunStrong.get())
        DI.sunModule()!!.sunSoft(sunSoft.get())
        DI.sunModule()!!.star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = ExtPlanetRollingComponentStoneComponent()
        DIPro.extOf(DI)
        val siriusStrong = WeakReference(Sirius())
        val siriusSoft = WeakReference(Sirius())
        val siriusWeak = WeakReference(Sirius())
        val moonStrong = WeakReference(Moon())
        val moonSoft = WeakReference(Moon())
        val moonWeak = WeakReference(Moon())
        DIPro.sunModule()!!.siriusStrong(siriusStrong.get())
        DIPro.sunModule()!!.siriusSoft(siriusSoft.get())
        DIPro.sunModule()!!.siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DI.gcStrong()

        //Then
        for (ref in listOf(
            earthStrong, planetWeak,
            sunStrong, starWeak,
            siriusStrong, siriusWeak,
            moonStrong, moonWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthSoft,
            sunSoft,
            siriusSoft,
            moonSoft
        )) {
            assertNotNull(ref.get())
        }
    }


    @Test
    fun gcSoftTest() {
        //Given
        val DI: PlanetRollingComponent = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule()!!.sunStrong(sunStrong.get())
        DI.sunModule()!!.sunSoft(sunSoft.get())
        DI.sunModule()!!.star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = ExtPlanetRollingComponentStoneComponent()
        DIPro.extOf(DI)
        val siriusStrong = WeakReference(Sirius())
        val siriusSoft = WeakReference(Sirius())
        val siriusWeak = WeakReference(Sirius())
        val moonStrong = WeakReference(Moon())
        val moonSoft = WeakReference(Moon())
        val moonWeak = WeakReference(Moon())
        DIPro.sunModule()!!.siriusStrong(siriusStrong.get())
        DIPro.sunModule()!!.siriusSoft(siriusSoft.get())
        DIPro.sunModule()!!.siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DI.gcSoft()

        //Then
        for (ref in listOf(
            earthSoft, planetWeak,
            sunSoft, starWeak,
            siriusSoft, siriusWeak,
            moonSoft, moonWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong,
            sunStrong,
            siriusStrong,
            moonStrong
        )) {
            assertNotNull(ref.get())
        }
    }


    @Test
    fun gcWeakTest() {
        //Given
        val DI: PlanetRollingComponent = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule()!!.sunStrong(sunStrong.get())
        DI.sunModule()!!.sunSoft(sunSoft.get())
        DI.sunModule()!!.star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = ExtPlanetRollingComponentStoneComponent()
        DIPro.extOf(DI)
        val siriusStrong = WeakReference(Sirius())
        val siriusSoft = WeakReference(Sirius())
        val siriusWeak = WeakReference(Sirius())
        val moonStrong = WeakReference(Moon())
        val moonSoft = WeakReference(Moon())
        val moonWeak = WeakReference(Moon())
        DIPro.sunModule()!!.siriusStrong(siriusStrong.get())
        DIPro.sunModule()!!.siriusSoft(siriusSoft.get())
        DIPro.sunModule()!!.siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DI.gcWeak()

        //Then
        for (ref in listOf(
            planetWeak,
            starWeak,
            siriusWeak,
            moonWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong, earthSoft,
            sunStrong, sunSoft,
            siriusStrong, siriusSoft,
            moonStrong, moonSoft
        )) {
            assertNotNull(ref.get())
        }
    }


    @Test
    fun gcSoftSunTest() {
        //Given
        val DI: PlanetRollingComponent = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule()!!.sunStrong(sunStrong.get())
        DI.sunModule()!!.sunSoft(sunSoft.get())
        DI.sunModule()!!.star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = ExtPlanetRollingComponentStoneComponent()
        DIPro.extOf(DI)
        val siriusStrong = WeakReference(Sirius())
        val siriusSoft = WeakReference(Sirius())
        val siriusWeak = WeakReference(Sirius())
        val moonStrong = WeakReference(Moon())
        val moonSoft = WeakReference(Moon())
        val moonWeak = WeakReference(Moon())
        DIPro.sunModule()!!.siriusStrong(siriusStrong.get())
        DIPro.sunModule()!!.siriusSoft(siriusSoft.get())
        DIPro.sunModule()!!.siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DI.gcSoftSun()

        //Then
        for (ref in listOf(
            planetWeak,
            sunSoft, starWeak,
            siriusWeak,
            moonWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong, earthSoft,
            sunStrong,
            siriusStrong, siriusSoft,
            moonStrong, moonSoft
        )) {
            assertNotNull(ref.get())
        }
    }


    @Test
    fun gcSoftPlanetsTest() {
        //Given
        val DI: PlanetRollingComponent = PlanetRollingComponentStoneComponent()
        val earthStrong = WeakReference(Earth())
        val earthSoft = WeakReference(Earth())
        val planetWeak = WeakReference(Earth())
        val sunStrong = WeakReference(Sun())
        val sunSoft = WeakReference(Sun())
        val starWeak = WeakReference(Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule()!!.sunStrong(sunStrong.get())
        DI.sunModule()!!.sunSoft(sunSoft.get())
        DI.sunModule()!!.star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = ExtPlanetRollingComponentStoneComponent()
        DIPro.extOf(DI)
        val siriusStrong = WeakReference(Sirius())
        val siriusSoft = WeakReference(Sirius())
        val siriusWeak = WeakReference(Sirius())
        val moonStrong = WeakReference(Moon())
        val moonSoft = WeakReference(Moon())
        val moonWeak = WeakReference(Moon())
        DIPro.sunModule()!!.siriusStrong(siriusStrong.get())
        DIPro.sunModule()!!.siriusSoft(siriusSoft.get())
        DIPro.sunModule()!!.siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DI.gcSoftPlanets()

        //Then
        for (ref in listOf(
            earthSoft, planetWeak,
            starWeak,
            siriusWeak,
            moonWeak
        )) {
            assertNull(ref.get())
        }

        for (ref in listOf(
            earthStrong,
            sunStrong, sunSoft,
            siriusStrong, siriusSoft,
            moonStrong, moonSoft
        )) {
            assertNotNull(ref.get())
        }
    }
}
