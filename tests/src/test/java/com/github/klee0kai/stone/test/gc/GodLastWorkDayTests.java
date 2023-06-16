package com.github.klee0kai.stone.test.gc;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.gcforest.GcGodComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class GodLastWorkDayTests {


    @Test
    void gcAllTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcAll();

        //Then
        assertNull(sunRef.get());
        assertNull(earthRef.get());
        assertNull(saturnRef.get());
    }


    @Test
    void gcWeakTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcWeak();

        //Then
        assertNotNull(sunRef.get());
        assertNotNull(earthRef.get());
        assertNull(saturnRef.get());
    }


    @Test
    void gcSoftTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcSoft();

        //Then
        assertNotNull(sunRef.get());
        assertNull(earthRef.get());
        assertNull(saturnRef.get());
    }

    @Test
    void gcStrongTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcStrong();

        //Then
        assertNull(sunRef.get());
        assertNotNull(earthRef.get());
        assertNull(saturnRef.get());
    }


    @Test
    void gcSunTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcSun();

        //Then
        assertNull(sunRef.get());
        assertNotNull(earthRef.get());
        assertNull(saturnRef.get());
    }


    @Test
    void gcPlanetsTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcPlanets();

        //Then
        assertNotNull(sunRef.get());
        assertNull(earthRef.get());
        assertNull(saturnRef.get());
    }

    @Test
    void gcSunAndPlanetsTest() {
        //Given
        GcGodComponent di = Stone.createComponent(GcGodComponent.class);
        WeakReference<Sun> sunRef = new WeakReference<>(new Sun());
        WeakReference<Earth> earthRef = new WeakReference<>(new Earth());
        WeakReference<Saturn> saturnRef = new WeakReference<>(new Saturn());
        di.__bind(sunRef.get(), earthRef.get(), saturnRef.get());

        //When
        di.gcSunAndPlanets();

        //Then
        assertNull(sunRef.get());
        assertNull(earthRef.get());
        assertNull(saturnRef.get());
    }
}
