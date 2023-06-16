package com.dirgub.klee0kai.stone.text_ext.bindinstance.singlemethod;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.PlanetSputnikComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StarProvideTests {

    @Test
    public void sunReusableTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Sun sun = new Sun();
        DI.sunModule().sun(sun);

        //When
        PlanetSputnikComponent DIPro = Stone.createComponent(PlanetSputnikComponent.class);
        DIPro.extendOf(DI);


        //Then
        assertEquals(sun, DI.sunModule().sun(null));
        assertEquals(sun, DI.sunModule().sun(null));
        assertNull(DI.sunModule().star(null));
        assertNull(DIPro.sunModule().star(null));
    }


    @Test
    public void createAfterExtendTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        PlanetSputnikComponent DIPro = Stone.createComponent(PlanetSputnikComponent.class);
        DIPro.extendOf(DI);
        Sun sun = new Sun();

        //When
        DI.sunModule().sun(sun);


        //Then
        assertEquals(sun, DI.sunModule().sun(null));
        assertEquals(sun, DI.sunModule().sun(null));
        assertNull(DI.sunModule().star(null));
        assertNull(DIPro.sunModule().star(null));
    }


    @Test
    public void extendedSunTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Sun sun1 = new Sun();
        Sun sun2 = new Sun();
        DI.sunModule().sun(sun1);

        //When
        PlanetSputnikComponent DIPro = Stone.createComponent(PlanetSputnikComponent.class);
        DIPro.extendOf(DI);
        DIPro.sunModule().sun(sun2);

        //Then
        assertEquals(sun2, DI.sunModule().sun(null));
        assertEquals(sun2, DI.sunModule().sun(null));
        assertNull(DI.sunModule().star(null));
        assertNull(DIPro.sunModule().star(null));
    }

    @Test
    public void updatedAfterExtendSunTest() {
        //Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Sun sun1 = new Sun();
        Sun sun2 = new Sun();
        Sun sun3 = new Sun();
        DI.sunModule().sun(sun1);
        PlanetSputnikComponent DIPro = Stone.createComponent(PlanetSputnikComponent.class);
        DIPro.extendOf(DI);
        DIPro.sunModule().sun(sun2);

        //When
        DI.bindSun(sun3);


        //Then
        assertEquals(sun3, DI.sunModule().sun(null));
        assertEquals(sun3, DI.sunModule().sun(null));
        assertNull(DI.sunModule().star(null));
        assertNull(DIPro.sunModule().star(null));
    }


}
