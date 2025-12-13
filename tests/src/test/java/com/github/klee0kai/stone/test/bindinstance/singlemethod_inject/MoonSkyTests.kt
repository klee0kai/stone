package com.github.klee0kai.stone.test.bindinstance.singlemethod_inject;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod_inject.StarSkyComponent;
import com.github.klee0kai.test.mowgli.MoonSky;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Mercury;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoonSkyTests {

    @Test
    public void moonSkyTest() {
        //Given
        StarSkyComponent component = Stone.createComponent(StarSkyComponent.class);
        Sun sun = new Sun();
        Sun star = new Sun();
        Earth earth = new Earth();
        Mercury planet = new Mercury();
        component.starModule().sun(sun);
        component.starModule().star(star);
        component.earth(earth);
        component.planet(planet);

        //When
        MoonSky moonSky = new MoonSky();
        component.inject(moonSky);

        //Then
        assertEquals(sun, moonSky.sun);
        assertEquals(star, moonSky.star);
        assertEquals(earth, moonSky.earth);
        assertEquals(planet, moonSky.planet);
    }

}
