package com.github.klee0kai.stone.test.bindinstance.solarsystem;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.solarsystem.SolarSystemComponent;
import com.github.klee0kai.test.mowgli.galaxy.SolarSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SolarSystemTests {

    @Test
    public void provideSolarSystemTest() {
        //Given
        SolarSystemComponent DI = Stone.createComponent(SolarSystemComponent.class);
        SolarSystem solarSystem = new SolarSystem();

        // When
        DI.bind(solarSystem);

        // Then
        assertEquals(solarSystem, DI.bind(null));
    }


    @Test
    public void provideEarthTest() {
        //Given
        SolarSystemComponent DI = Stone.createComponent(SolarSystemComponent.class);
        SolarSystem solarSystem = new SolarSystem();

        // When
        DI.bind(solarSystem);

        // Then
        assertNotNull(DI.earth());
    }


}
