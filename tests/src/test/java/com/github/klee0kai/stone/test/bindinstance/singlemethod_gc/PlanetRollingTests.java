package com.github.klee0kai.stone.test.bindinstance.singlemethod_gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlanetRollingTests {

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

        //When
        DI.gcAll();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthStrong, earthSoft, planetWeak,
                sunStrong, sunSoft, starWeak
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

        //When
        DI.gcStrong();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthStrong, planetWeak,
                sunStrong, starWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthSoft,
                sunSoft
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

        //When
        DI.gcSoft();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthSoft, planetWeak,
                sunSoft, starWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong,
                sunStrong
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

        //When
        DI.gcWeak();

        //Then
        for (WeakReference ref : Arrays.asList(
                planetWeak,
                starWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong, earthSoft,
                sunStrong, sunSoft
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

        //When
        DI.gcSoftSun();

        //Then
        for (WeakReference ref : Arrays.asList(
                planetWeak,
                sunSoft, starWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong, earthSoft,
                sunStrong
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

        //When
        DI.gcSoftPlanets();

        //Then
        for (WeakReference ref : Arrays.asList(
                earthSoft, planetWeak,
                starWeak
        )) {
            assertNull(ref.get());
        }

        for (WeakReference ref : Arrays.asList(
                earthStrong,
                sunStrong, sunSoft
        )) {
            assertNotNull(ref.get());
        }
    }


}
