package com.github.klee0kai.stone.test.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test.mowgli.galaxy.Saturn;
import com.github.klee0kai.test.mowgli.galaxy.Sun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GodFirstWorkDayTest {

    @Test
    public void firstCreateSunTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();

        //When
        DI.bindSun(sun);

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid);
        assertNull(DI.sunSystem().earth());
    }


    @Test
    public void createSunAndEarthTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        Earth earth = new Earth();

        //When
        DI.bindSun(sun);
        DI.bindEarth(earth);

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid);
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid);
        assertNull(DI.sunSystem().planet());
    }


    @Test
    public void createSunEarthSaturnTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        Earth earth = new Earth();
        Saturn saturn = new Saturn();

        //When
        DI.bindSun(sun);
        DI.bindEarth(earth);
        DI.bindSaturn(saturn);

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid);
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid);
        assertEquals(saturn.uuid, DI.sunSystem().saturn().uuid);
        assertNull(DI.sunSystem().planet());
    }


    @Test
    public void createSaturnEarthSunTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        Earth earth = new Earth();
        Saturn saturn = new Saturn();

        //When
        DI.bindSaturn(saturn);
        DI.bindEarth(earth);
        DI.bindSun(sun);

        //Then
        assertEquals(sun.uuid, DI.sunSystem().sun().uuid);
        assertEquals(earth.uuid, DI.sunSystem().earth().uuid);
        assertEquals(saturn.uuid, DI.sunSystem().saturn().uuid);
        assertNull(DI.sunSystem().planet());
    }


}
