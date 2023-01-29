package com.dirgub.klee0kai.stone.text_ext.inject.bindInstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test_ext.inject.di.bindinstance.simple.GodRebrandingComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GodRebrandingPlanetTest {

    @Test
    public void createdIsReusableTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Earth earth = new Earth();
        DI.bindPlanet(earth);


        //When
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extOf(DI);


        //Then
        assertEquals(earth, DI.sunSystem().planet());
        assertEquals(earth, DIPro.sunSystem().planet());
        assertNull(DI.sunSystem().earth());
        assertNull(DIPro.sunSystem().earth());
    }

    @Test
    public void createdAfterRebrandingTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extOf(DI);
        Earth earth = new Earth();


        //When
        DI.bindPlanet(earth);


        //Then
        assertEquals(earth, DI.sunSystem().planet());
        assertEquals(earth, DIPro.sunSystem().planet());
        assertNull(DI.sunSystem().earth());
        assertNull(DIPro.sunSystem().earth());
    }


    @Test
    public void rebrandedSunTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        DI.bindPlanet(earth1);


        //When
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extOf(DI);
        DIPro.bindPlanet(earth2);


        //Then
        assertEquals(earth2, DI.sunSystem().planet());
        assertEquals(earth2, DIPro.sunSystem().planet());
        assertNull(DI.sunSystem().earth());
        assertNull(DIPro.sunSystem().earth());
    }


    @Test
    public void updatedAfterRebrandingSunTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        Earth earth3 = new Earth();
        DI.bindPlanet(earth1);
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extOf(DI);
        DIPro.bindPlanet(earth2);


        //When
        DI.bindPlanet(earth3);


        //Then
        assertEquals(earth3, DI.sunSystem().planet());
        assertEquals(earth3, DIPro.sunSystem().planet());
        assertNull(DI.sunSystem().earth());
        assertNull(DIPro.sunSystem().earth());
    }

}
