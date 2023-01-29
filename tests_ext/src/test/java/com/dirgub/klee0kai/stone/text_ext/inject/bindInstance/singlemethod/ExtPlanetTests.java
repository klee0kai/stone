package com.dirgub.klee0kai.stone.text_ext.inject.bindInstance.singlemethod;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.singlemethod.PlanetComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test_ext.inject.di.bindinstance.singlemethod.ExtPlanetComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExtPlanetTests {

    @Test
    public void createdIsReusableTest() {
        // Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Earth earth = new Earth();
        DI.earth(earth);


        //When
        ExtPlanetComponent DIPro = Stone.createComponent(ExtPlanetComponent.class);
        DIPro.extOf(DI);


        //Then
        assertEquals(earth, DI.earth(null));
        assertEquals(earth, DIPro.earth(null));
        assertNull(DI.planet(null));
        assertNull(DIPro.planet(null));
    }

    @Test
    public void createdAfterExtendTest() {
        // Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        ExtPlanetComponent DIPro = Stone.createComponent(ExtPlanetComponent.class);
        DIPro.extOf(DI);
        Earth earth = new Earth();


        //When
        DI.bind(earth);


        //Then
        assertEquals(earth, DI.earth(null));
        assertEquals(earth, DIPro.earth(null));
        assertNull(DI.planet(null));
        assertNull(DIPro.planet(null));
    }


    @Test
    public void extendedEarthTest() {
        // Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        DI.bind(earth1);


        //When
        ExtPlanetComponent DIPro = Stone.createComponent(ExtPlanetComponent.class);
        DIPro.extOf(DI);
        DIPro.bind(earth2);


        //Then
        assertEquals(earth2, DI.earth(null));
        assertEquals(earth2, DIPro.earth(null));
        assertNull(DI.planet(null));
        assertNull(DIPro.planet(null));
    }


    @Test
    public void updatedAfterExtendEarthTest() {
        // Given
        PlanetComponent DI = Stone.createComponent(PlanetComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        Earth earth3 = new Earth();
        DI.bind(earth1);
        ExtPlanetComponent DIPro = Stone.createComponent(ExtPlanetComponent.class);
        DIPro.extOf(DI);
        DIPro.bind(earth2);


        //When
        DI.bind(earth3);


        //Then
        assertEquals(earth3, DI.earth(null));
        assertEquals(earth3, DIPro.earth(null));
        assertNull(DI.planet(null));
        assertNull(DIPro.planet(null));
    }


}
