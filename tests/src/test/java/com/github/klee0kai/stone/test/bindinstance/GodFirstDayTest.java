package com.github.klee0kai.stone.test.bindinstance;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.GodWorkspaceComponent;
import com.github.klee0kai.test.mowgli.world.Earth;
import com.github.klee0kai.test.mowgli.world.Saturn;
import com.github.klee0kai.test.mowgli.world.Sun;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GodFirstDayTest {

    @Test
    public void firstCreateSunTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();

        //When
        DI.bind(sun);

        //Then
        assertEquals(sun.uuid, DI.world().sun().uuid);
        assertNull(DI.world().earth());
    }


    @Test
    public void createSunAndEarthTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        Earth earth = new Earth();

        //When
        DI.bind(sun, earth);

        //Then
        assertEquals(sun.uuid, DI.world().sun().uuid);
        assertEquals(earth.uuid, DI.world().earth().uuid);
        assertNull(DI.world().planet());
    }


    @Test
    public void createSunEarthSaturnTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        Earth earth = new Earth();
        Saturn saturn = new Saturn();

        //When
        DI.bind(sun, earth,saturn);

        //Then
        assertEquals(sun.uuid, DI.world().sun().uuid);
        assertEquals(earth.uuid, DI.world().earth().uuid);
        assertEquals(saturn.uuid, DI.world().saturn().uuid);
        assertNull(DI.world().planet());
    }


    @Test
    public void createSaturnEarthSunTest() {
        //Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Sun sun = new Sun();
        Earth earth = new Earth();
        Saturn saturn = new Saturn();

        //When
        DI.bind(saturn);
        DI.bind(earth);
        DI.bind(sun);

        //Then
        assertEquals(sun.uuid, DI.world().sun().uuid);
        assertEquals(earth.uuid, DI.world().earth().uuid);
        assertEquals(saturn.uuid, DI.world().saturn().uuid);
        assertNull(DI.world().planet());
    }


}
