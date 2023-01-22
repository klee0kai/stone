package com.github.klee0kai.stone.test.bindinstance.singlemethod_inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponent;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MoonSkyProtectInjectedTests {

    @Test
    public void withoutProtectInjectedTest() {
        //Given
        StarSkyComponent component = Stone.createComponent(StarSkyComponent.class);
        WeakReference<Mercury> mercury = new WeakReference<>(new Mercury());
        WeakReference<Sun> star = new WeakReference<>(new Sun());
        component.starModule().star(star.get());
        component.mercury(mercury.get());

        //When
        MoonSky moonSky = new MoonSky();
        component.inject(moonSky);
        moonSky = null;
        component.gcAll();


        //Then
        assertNull(mercury.get());
        assertNull(star.get());
    }


    @Test
    public void withProtectInjectedTest() throws InterruptedException {
        //Given
        System.gc();
        StarSkyComponent component = Stone.createComponent(StarSkyComponent.class);
        WeakReference<Mercury> mercury = new WeakReference<>(new Mercury());
        WeakReference<Sun> star = new WeakReference<>(new Sun());
        component.starModule().star(star.get());
        component.mercury(mercury.get());

        //When
        MoonSky moonSky = new MoonSky();
        component.inject(moonSky);
        component.protectInjected(moonSky);
        moonSky = null;
        component.gcAll();

        assertNotNull(mercury.get());
        assertNotNull(star.get());

        //Then after protect finished
        Thread.sleep(100);
        System.gc();
        assertNull(mercury.get());
        assertNull(star.get());
    }
}
