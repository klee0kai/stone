package com.github.klee0kai.stone.test.bindinstance.simple_inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.simple_inject.SevenPlanetComponent;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SevenPlanetTests {

    @Test
    public void moonSkyTest() {
        //Given
        SevenPlanetComponent component = Stone.createComponent(SevenPlanetComponent.class);
        Earth earth = new Earth();
        Mercury mercury = new Mercury();
        Earth planet = new Earth();
        component.__bind(earth, mercury);
        component.bindPlanet(planet);

        //When
        MoonSky moonSky = new MoonSky();
        component.inject(moonSky);

        //Then
        assertEquals(earth, moonSky.earth);
        assertEquals(mercury, moonSky.mercury);
        assertEquals(planet, moonSky.planet);
    }

}
