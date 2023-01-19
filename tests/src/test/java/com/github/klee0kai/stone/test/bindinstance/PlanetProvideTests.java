package com.github.klee0kai.stone.test.bindinstance;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.PlanetComponent;
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
        DI.bind(earth);

        //Then
        assertEquals(earth, DI.earth(null));
        assertEquals(earth, DI.earth(null));
        assertNull(DI.planet(null));
        assertNull(DI.providePlanet());
    }


}
