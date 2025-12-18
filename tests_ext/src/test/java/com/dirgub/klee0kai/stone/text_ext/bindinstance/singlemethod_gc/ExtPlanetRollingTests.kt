package com.dirgub.klee0kai.stone.text_ext.bindinstance.singlemethod_gc

import com.github.klee0kai.stone.Stone
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

class ExtPlanetRollingTests {

    @Test
    fun gcAllTest() {
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
        DIPro.gcAllExt()

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
        DIPro.gcStrongExt()

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
        DIPro.gcSoftExt()

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


    @org.junit.jupiter.api.Test
    fun gcWeakTest() {
        //Given
        val DI: PlanetRollingComponent = Stone.createComponent(PlanetRollingComponent::class.java)
        val earthStrong: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val earthSoft: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val planetWeak: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val sunStrong =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val sunSoft =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val starWeak =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = Stone.createComponent(ExtPlanetRollingComponent::class.java)
        DIPro.extOf(DI)
        val siriusStrong: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusSoft: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusWeak: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val moonStrong: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonSoft: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonWeak: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        DIPro.sunModule().siriusStrong(siriusStrong.get())
        DIPro.sunModule().siriusSoft(siriusSoft.get())
        DIPro.sunModule().siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DIPro.gcWeakExt()

        //Then
        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            planetWeak,
            starWeak,
            siriusWeak,
            moonWeak
        )) {
            org.junit.jupiter.api.Assertions.assertNull(ref.get())
        }

        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            earthStrong, earthSoft,
            sunStrong, sunSoft,
            siriusStrong, siriusSoft,
            moonStrong, moonSoft
        )) {
            org.junit.jupiter.api.Assertions.assertNotNull(ref.get())
        }
    }


    @org.junit.jupiter.api.Test
    fun gcSoftSunTest() {
        //Given
        val DI: PlanetRollingComponent = Stone.createComponent(PlanetRollingComponent::class.java)
        val earthStrong: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val earthSoft: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val planetWeak: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val sunStrong =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val sunSoft =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val starWeak =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = Stone.createComponent(ExtPlanetRollingComponent::class.java)
        DIPro.extOf(DI)
        val siriusStrong: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusSoft: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusWeak: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val moonStrong: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonSoft: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonWeak: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        DIPro.sunModule().siriusStrong(siriusStrong.get())
        DIPro.sunModule().siriusSoft(siriusSoft.get())
        DIPro.sunModule().siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DIPro.gcSoftSunExt()

        //Then
        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            planetWeak,
            sunSoft, starWeak,
            siriusWeak,
            moonWeak
        )) {
            org.junit.jupiter.api.Assertions.assertNull(ref.get())
        }

        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            earthStrong, earthSoft,
            sunStrong,
            siriusStrong, siriusSoft,
            moonStrong, moonSoft
        )) {
            org.junit.jupiter.api.Assertions.assertNotNull(ref.get())
        }
    }


    @org.junit.jupiter.api.Test
    fun gcSoftPlanetsTest() {
        //Given
        val DI: PlanetRollingComponent = Stone.createComponent(PlanetRollingComponent::class.java)
        val earthStrong: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val earthSoft: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val planetWeak: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val sunStrong =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val sunSoft =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val starWeak =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = Stone.createComponent(ExtPlanetRollingComponent::class.java)
        DIPro.extOf(DI)
        val siriusStrong: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusSoft: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusWeak: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val moonStrong: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonSoft: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonWeak: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        DIPro.sunModule().siriusStrong(siriusStrong.get())
        DIPro.sunModule().siriusSoft(siriusSoft.get())
        DIPro.sunModule().siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DIPro.gcSoftPlanetsExt()

        //Then
        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            earthSoft, planetWeak,
            starWeak,
            siriusWeak,
            moonWeak
        )) {
            org.junit.jupiter.api.Assertions.assertNull(ref.get())
        }

        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            earthStrong,
            sunStrong, sunSoft,
            siriusStrong, siriusSoft,
            moonStrong, moonSoft
        )) {
            org.junit.jupiter.api.Assertions.assertNotNull(ref.get())
        }
    }


    @org.junit.jupiter.api.Test
    fun gcSoftSiriusTest() {
        //Given
        val DI: PlanetRollingComponent = Stone.createComponent(PlanetRollingComponent::class.java)
        val earthStrong: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val earthSoft: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val planetWeak: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val sunStrong =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val sunSoft =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val starWeak =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = Stone.createComponent(ExtPlanetRollingComponent::class.java)
        DIPro.extOf(DI)
        val siriusStrong: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusSoft: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusWeak: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val moonStrong: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonSoft: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonWeak: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        DIPro.sunModule().siriusStrong(siriusStrong.get())
        DIPro.sunModule().siriusSoft(siriusSoft.get())
        DIPro.sunModule().siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DIPro.gcSoftSiriusExt()

        //Then
        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            planetWeak,
            starWeak,
            siriusSoft, siriusWeak,
            moonWeak
        )) {
            org.junit.jupiter.api.Assertions.assertNull(ref.get())
        }

        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            earthStrong, earthSoft,
            sunStrong, sunSoft,
            siriusStrong,
            moonStrong, moonSoft
        )) {
            org.junit.jupiter.api.Assertions.assertNotNull(ref.get())
        }
    }


    @org.junit.jupiter.api.Test
    fun gcSputniksMoonTest() {
        //Given
        val DI: PlanetRollingComponent = Stone.createComponent(PlanetRollingComponent::class.java)
        val earthStrong: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val earthSoft: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val planetWeak: java.lang.ref.WeakReference<Earth?> = java.lang.ref.WeakReference<Earth?>(Earth())
        val sunStrong =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val sunSoft =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        val starWeak =
            java.lang.ref.WeakReference<com.github.klee0kai.test.mowgli.galaxy.Sun?>(com.github.klee0kai.test.mowgli.galaxy.Sun())
        DI.earthStrong(earthStrong.get())
        DI.earthSoft(earthSoft.get())
        DI.planet(planetWeak.get())
        DI.sunModule().sunStrong(sunStrong.get())
        DI.sunModule().sunSoft(sunSoft.get())
        DI.sunModule().star(starWeak.get())
        //external component init
        val DIPro: ExtPlanetRollingComponent = Stone.createComponent(ExtPlanetRollingComponent::class.java)
        DIPro.extOf(DI)
        val siriusStrong: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusSoft: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val siriusWeak: java.lang.ref.WeakReference<Sirius?> = java.lang.ref.WeakReference<Sirius?>(Sirius())
        val moonStrong: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonSoft: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        val moonWeak: java.lang.ref.WeakReference<Moon?> = java.lang.ref.WeakReference<Moon?>(Moon())
        DIPro.sunModule().siriusStrong(siriusStrong.get())
        DIPro.sunModule().siriusSoft(siriusSoft.get())
        DIPro.sunModule().siriusWeak(siriusWeak.get())
        DIPro.moonStrong(moonStrong.get())
        DIPro.moonSoft(moonSoft.get())
        DIPro.moonWeak(moonWeak.get())

        //When
        DIPro.gcSoftSputniksExt()

        //Then
        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            planetWeak,
            starWeak,
            siriusWeak,
            moonSoft, moonWeak
        )) {
            org.junit.jupiter.api.Assertions.assertNull(ref.get())
        }

        for (ref in java.util.Arrays.asList<java.lang.ref.WeakReference<out kotlin.Any?>>(
            earthStrong, earthSoft,
            sunStrong, sunSoft,
            siriusStrong, siriusSoft,
            moonStrong
        )) {
            org.junit.jupiter.api.Assertions.assertNotNull(ref.get())
        }
    }
}
