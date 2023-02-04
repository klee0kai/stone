package com.dirgub.klee0kai.stone.text_ext.bindinstance.singlemethod_gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod_gc.ExtPlanetRollingComponent;
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.sputniks.Moon;
import com.github.klee0kai.test_ext.inject.mowgli.galaxy.stars.Sirius;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlanetRollingAndProTests {

    @Test
    public void gcAllTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        WeakReference<Earth> earthStrong = new WeakReference<>(new Earth());
        WeakReference<Earth> earthSoft = new WeakReference<>(new Earth());
        WeakReference<Earth> planetWeak = new WeakReference<>(new Earth());
        WeakReference<Sun> sunStrong = new WeakReference<>(new Sun());
        WeakReference<Sun> sunSoft = new WeakReference<>(new Sun());
        WeakReference<Sun> starWeak = new WeakReference<>(new Sun());
        DI.earthStrong(earthStrong.get());
        DI.earthSoft(earthSoft.get());
        DI.planet(planetWeak.get());
        DI.sunModule().sunStrong(sunStrong.get());
        DI.sunModule().sunSoft(sunSoft.get());
        DI.sunModule().star(starWeak.get());
        //external component init
        ExtPlanetRollingComponent DIPro = Stone.createComponent(ExtPlanetRollingComponent.class);
        DIPro.extOf(DI);
        WeakReference<Sirius> siriusStrong = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusSoft = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusWeak = new WeakReference<>(new Sirius());
        WeakReference<Moon> moonStrong = new WeakReference<>(new Moon());
        WeakReference<Moon> moonSoft = new WeakReference<>(new Moon());
        WeakReference<Moon> moonWeak = new WeakReference<>(new Moon());
        DIPro.sunModule().siriusStrong(siriusStrong.get());
        DIPro.sunModule().siriusSoft(siriusSoft.get());
        DIPro.sunModule().siriusWeak(siriusWeak.get());
        DIPro.moonStrong(moonStrong.get());
        DIPro.moonSoft(moonSoft.get());
        DIPro.moonWeak(moonWeak.get());

        //When
        DI.gcAll();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthStrong, earthSoft, planetWeak,
                sunStrong, sunSoft, starWeak,
                siriusStrong, siriusSoft, siriusWeak,
                moonStrong, moonSoft, moonWeak
        )) {
            assertNull(ref.get());
        }
    }


    @Test
    public void gcStrongTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        WeakReference<Earth> earthStrong = new WeakReference<>(new Earth());
        WeakReference<Earth> earthSoft = new WeakReference<>(new Earth());
        WeakReference<Earth> planetWeak = new WeakReference<>(new Earth());
        WeakReference<Sun> sunStrong = new WeakReference<>(new Sun());
        WeakReference<Sun> sunSoft = new WeakReference<>(new Sun());
        WeakReference<Sun> starWeak = new WeakReference<>(new Sun());
        DI.earthStrong(earthStrong.get());
        DI.earthSoft(earthSoft.get());
        DI.planet(planetWeak.get());
        DI.sunModule().sunStrong(sunStrong.get());
        DI.sunModule().sunSoft(sunSoft.get());
        DI.sunModule().star(starWeak.get());
        //external component init
        ExtPlanetRollingComponent DIPro = Stone.createComponent(ExtPlanetRollingComponent.class);
        DIPro.extOf(DI);
        WeakReference<Sirius> siriusStrong = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusSoft = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusWeak = new WeakReference<>(new Sirius());
        WeakReference<Moon> moonStrong = new WeakReference<>(new Moon());
        WeakReference<Moon> moonSoft = new WeakReference<>(new Moon());
        WeakReference<Moon> moonWeak = new WeakReference<>(new Moon());
        DIPro.sunModule().siriusStrong(siriusStrong.get());
        DIPro.sunModule().siriusSoft(siriusSoft.get());
        DIPro.sunModule().siriusWeak(siriusWeak.get());
        DIPro.moonStrong(moonStrong.get());
        DIPro.moonSoft(moonSoft.get());
        DIPro.moonWeak(moonWeak.get());

        //When
        DI.gcStrong();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthStrong, planetWeak,
                sunStrong, starWeak,
                siriusStrong, siriusWeak,
                moonStrong, moonWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthSoft,
                sunSoft,
                siriusSoft,
                moonSoft
        )) {
            assertNotNull(ref.get());
        }
    }


    @Test
    public void gcSoftTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        WeakReference<Earth> earthStrong = new WeakReference<>(new Earth());
        WeakReference<Earth> earthSoft = new WeakReference<>(new Earth());
        WeakReference<Earth> planetWeak = new WeakReference<>(new Earth());
        WeakReference<Sun> sunStrong = new WeakReference<>(new Sun());
        WeakReference<Sun> sunSoft = new WeakReference<>(new Sun());
        WeakReference<Sun> starWeak = new WeakReference<>(new Sun());
        DI.earthStrong(earthStrong.get());
        DI.earthSoft(earthSoft.get());
        DI.planet(planetWeak.get());
        DI.sunModule().sunStrong(sunStrong.get());
        DI.sunModule().sunSoft(sunSoft.get());
        DI.sunModule().star(starWeak.get());
        //external component init
        ExtPlanetRollingComponent DIPro = Stone.createComponent(ExtPlanetRollingComponent.class);
        DIPro.extOf(DI);
        WeakReference<Sirius> siriusStrong = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusSoft = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusWeak = new WeakReference<>(new Sirius());
        WeakReference<Moon> moonStrong = new WeakReference<>(new Moon());
        WeakReference<Moon> moonSoft = new WeakReference<>(new Moon());
        WeakReference<Moon> moonWeak = new WeakReference<>(new Moon());
        DIPro.sunModule().siriusStrong(siriusStrong.get());
        DIPro.sunModule().siriusSoft(siriusSoft.get());
        DIPro.sunModule().siriusWeak(siriusWeak.get());
        DIPro.moonStrong(moonStrong.get());
        DIPro.moonSoft(moonSoft.get());
        DIPro.moonWeak(moonWeak.get());

        //When
        DI.gcSoft();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthSoft, planetWeak,
                sunSoft, starWeak,
                siriusSoft, siriusWeak,
                moonSoft, moonWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong,
                sunStrong,
                siriusStrong,
                moonStrong
        )) {
            assertNotNull(ref.get());
        }
    }


    @Test
    public void gcWeakTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        WeakReference<Earth> earthStrong = new WeakReference<>(new Earth());
        WeakReference<Earth> earthSoft = new WeakReference<>(new Earth());
        WeakReference<Earth> planetWeak = new WeakReference<>(new Earth());
        WeakReference<Sun> sunStrong = new WeakReference<>(new Sun());
        WeakReference<Sun> sunSoft = new WeakReference<>(new Sun());
        WeakReference<Sun> starWeak = new WeakReference<>(new Sun());
        DI.earthStrong(earthStrong.get());
        DI.earthSoft(earthSoft.get());
        DI.planet(planetWeak.get());
        DI.sunModule().sunStrong(sunStrong.get());
        DI.sunModule().sunSoft(sunSoft.get());
        DI.sunModule().star(starWeak.get());
        //external component init
        ExtPlanetRollingComponent DIPro = Stone.createComponent(ExtPlanetRollingComponent.class);
        DIPro.extOf(DI);
        WeakReference<Sirius> siriusStrong = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusSoft = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusWeak = new WeakReference<>(new Sirius());
        WeakReference<Moon> moonStrong = new WeakReference<>(new Moon());
        WeakReference<Moon> moonSoft = new WeakReference<>(new Moon());
        WeakReference<Moon> moonWeak = new WeakReference<>(new Moon());
        DIPro.sunModule().siriusStrong(siriusStrong.get());
        DIPro.sunModule().siriusSoft(siriusSoft.get());
        DIPro.sunModule().siriusWeak(siriusWeak.get());
        DIPro.moonStrong(moonStrong.get());
        DIPro.moonSoft(moonSoft.get());
        DIPro.moonWeak(moonWeak.get());

        //When
        DI.gcWeak();

        //Then
        for (WeakReference ref : Arrays.asList(
                planetWeak,
                starWeak,
                siriusWeak,
                moonWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong, earthSoft,
                sunStrong, sunSoft,
                siriusStrong, siriusSoft,
                moonStrong, moonSoft
        )) {
            assertNotNull(ref.get());
        }
    }


    @Test
    public void gcSoftSunTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        WeakReference<Earth> earthStrong = new WeakReference<>(new Earth());
        WeakReference<Earth> earthSoft = new WeakReference<>(new Earth());
        WeakReference<Earth> planetWeak = new WeakReference<>(new Earth());
        WeakReference<Sun> sunStrong = new WeakReference<>(new Sun());
        WeakReference<Sun> sunSoft = new WeakReference<>(new Sun());
        WeakReference<Sun> starWeak = new WeakReference<>(new Sun());
        DI.earthStrong(earthStrong.get());
        DI.earthSoft(earthSoft.get());
        DI.planet(planetWeak.get());
        DI.sunModule().sunStrong(sunStrong.get());
        DI.sunModule().sunSoft(sunSoft.get());
        DI.sunModule().star(starWeak.get());
        //external component init
        ExtPlanetRollingComponent DIPro = Stone.createComponent(ExtPlanetRollingComponent.class);
        DIPro.extOf(DI);
        WeakReference<Sirius> siriusStrong = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusSoft = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusWeak = new WeakReference<>(new Sirius());
        WeakReference<Moon> moonStrong = new WeakReference<>(new Moon());
        WeakReference<Moon> moonSoft = new WeakReference<>(new Moon());
        WeakReference<Moon> moonWeak = new WeakReference<>(new Moon());
        DIPro.sunModule().siriusStrong(siriusStrong.get());
        DIPro.sunModule().siriusSoft(siriusSoft.get());
        DIPro.sunModule().siriusWeak(siriusWeak.get());
        DIPro.moonStrong(moonStrong.get());
        DIPro.moonSoft(moonSoft.get());
        DIPro.moonWeak(moonWeak.get());

        //When
        DI.gcSoftSun();

        //Then
        for (WeakReference ref : Arrays.asList(
                planetWeak,
                sunSoft, starWeak,
                siriusWeak,
                moonWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong, earthSoft,
                sunStrong,
                siriusStrong, siriusSoft,
                moonStrong, moonSoft
        )) {
            assertNotNull(ref.get());
        }
    }


    @Test
    public void gcSoftPlanetsTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        WeakReference<Earth> earthStrong = new WeakReference<>(new Earth());
        WeakReference<Earth> earthSoft = new WeakReference<>(new Earth());
        WeakReference<Earth> planetWeak = new WeakReference<>(new Earth());
        WeakReference<Sun> sunStrong = new WeakReference<>(new Sun());
        WeakReference<Sun> sunSoft = new WeakReference<>(new Sun());
        WeakReference<Sun> starWeak = new WeakReference<>(new Sun());
        DI.earthStrong(earthStrong.get());
        DI.earthSoft(earthSoft.get());
        DI.planet(planetWeak.get());
        DI.sunModule().sunStrong(sunStrong.get());
        DI.sunModule().sunSoft(sunSoft.get());
        DI.sunModule().star(starWeak.get());
        //external component init
        ExtPlanetRollingComponent DIPro = Stone.createComponent(ExtPlanetRollingComponent.class);
        DIPro.extOf(DI);
        WeakReference<Sirius> siriusStrong = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusSoft = new WeakReference<>(new Sirius());
        WeakReference<Sirius> siriusWeak = new WeakReference<>(new Sirius());
        WeakReference<Moon> moonStrong = new WeakReference<>(new Moon());
        WeakReference<Moon> moonSoft = new WeakReference<>(new Moon());
        WeakReference<Moon> moonWeak = new WeakReference<>(new Moon());
        DIPro.sunModule().siriusStrong(siriusStrong.get());
        DIPro.sunModule().siriusSoft(siriusSoft.get());
        DIPro.sunModule().siriusWeak(siriusWeak.get());
        DIPro.moonStrong(moonStrong.get());
        DIPro.moonSoft(moonSoft.get());
        DIPro.moonWeak(moonWeak.get());

        //When
        DI.gcSoftPlanets();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthSoft, planetWeak,
                starWeak,
                siriusWeak,
                moonWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong,
                sunStrong, sunSoft,
                siriusStrong, siriusSoft,
                moonStrong, moonSoft
        )) {
            assertNotNull(ref.get());
        }
    }


}
