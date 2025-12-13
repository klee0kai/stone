package com.github.klee0kai.stone.test.bindinstance.singlemethod;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent;
import com.github.klee0kai.test.di.bindinstance.singlemethod_gc.PlanetRollingComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlanetProvideTests {

    @Test
    public void bindPlanetTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Earth earth = new Earth();

        //When
        DI.planet(earth);

        //Then
        assertEquals(earth, DI.planet(null));
        assertEquals(earth, DI.planet(null));
        assertEquals(earth, DI.providePlanet());
        assertEquals(earth, DI.providePlanet());
        assertNull(DI.earth(null));
    }

    @Test
    public void bindEarthTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Earth earth = new Earth();

        //When
        DI.earth(earth);

        //Then
        assertEquals(earth, DI.earth(null));
        assertEquals(earth, DI.earth(null));
        assertNull(DI.planet(null));
        assertNull(DI.providePlanet());
    }

    @Test
    public void bindEarthCommonTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Earth earth = new Earth();

        //When
        DI.earth(earth);

        //Then
        assertEquals(earth, DI.earth(null));
        assertEquals(earth, DI.earth(null));
        assertNull(DI.planet(null));
        assertNull(DI.providePlanet());
    }


    @Test
    public void separateBindEarthTest() {
        //Given
        PlanetRollingComponent DI = Stone.createComponent(PlanetRollingComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        Earth earth3 = new Earth();

        //When
        DI.earthStrong(earth1);
        DI.earthSoft(earth2);
        DI.earth(earth3);

        //Then
        assertEquals(earth1, DI.earthStrong(null));
        assertEquals(earth2, DI.earthSoft(null));
        assertEquals(earth3, DI.earth(null));
    }

}
