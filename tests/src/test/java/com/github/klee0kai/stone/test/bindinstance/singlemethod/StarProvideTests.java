package com.github.klee0kai.stone.test.bindinstance.singlemethod;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StarProvideTests {

    @Test
    public void bindSunTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Sun sun = new Sun();

        //When
        DI.sunModule().sun(sun);

        //Then
        assertEquals(sun, DI.sunModule().sun(null));
        assertEquals(sun, DI.sunModule().sun(null));
        assertNull(DI.sunModule().star(null));
    }

    @Test
    public void bindStarTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Sun sun = new Sun();

        //When
        DI.sunModule().star(sun);

        //Then
        assertEquals(sun, DI.sunModule().star(null));
        assertEquals(sun, DI.sunModule().star(null));
        assertNull(DI.sunModule().sun(null));
    }


    @Test
    public void separateSunBindingTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Sun sun1 = new Sun();
        Sun sun2 = new Sun();
        Sun sun3 = new Sun();

        //When
        DI.sunModule().sunStrong(sun1);
        DI.sunModule().sunSoft(sun2);
        DI.sunModule().sun(sun3);

        //Then
        assertEquals(sun1, DI.sunModule().sunStrong(null));
        assertEquals(sun2, DI.sunModule().sunSoft(null));
        assertEquals(sun3, DI.sunModule().sun(null));
    }


}
