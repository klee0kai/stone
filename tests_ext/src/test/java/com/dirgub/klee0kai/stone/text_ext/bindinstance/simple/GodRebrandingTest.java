package com.dirgub.klee0kai.stone.text_ext.bindinstance.simple;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.bindinstance.simple.GodWorkspaceComponent;
import com.github.klee0kai.test.mowgli.galaxy.Earth;
import com.github.klee0kai.test_ext.inject.di.bindinstance.simple.GodRebrandingComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GodRebrandingTest {

    @Test
    public void createdIsReusableTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Earth earth = new Earth();
        DI.bindEarth(earth);


        //When
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extendComponent(DI);


        //Then
        assertEquals(earth, DI.sunSystem().earth());
        assertEquals(earth, DIPro.sunSystem().earth());
        assertNull(DI.sunSystem().planet());
        assertNull(DIPro.sunSystem().planet());
    }

    @Test
    public void createdAfterRebrandingTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extendComponent(DI);
        Earth earth = new Earth();


        //When
        DI.bindEarth(earth);


        //Then
        assertEquals(earth, DI.sunSystem().earth());
        assertEquals(earth, DIPro.sunSystem().earth());
        assertNull(DI.sunSystem().planet());
        assertNull(DIPro.sunSystem().planet());
    }


    @Test
    public void rebrandedSunTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        DI.bindEarth(earth1);


        //When
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extendComponent(DI);
        DIPro.bindEarth(earth2);


        //Then
        assertEquals(earth2, DI.sunSystem().earth());
        assertEquals(earth2, DIPro.sunSystem().earth());
        assertNull(DI.sunSystem().planet());
        assertNull(DIPro.sunSystem().planet());
    }


    @Test
    public void updatedAfterRebrandingSunTest() {
        // Given
        GodWorkspaceComponent DI = Stone.createComponent(GodWorkspaceComponent.class);
        Earth earth1 = new Earth();
        Earth earth2 = new Earth();
        Earth earth3 = new Earth();
        DI.bindEarth(earth1);
        GodRebrandingComponent DIPro = Stone.createComponent(GodRebrandingComponent.class);
        DIPro.extendComponent(DI);
        DIPro.bindEarth(earth2);


        //When
        DI.bindEarth(earth3);


        //Then
        assertEquals(earth3, DI.sunSystem().earth());
        assertEquals(earth3, DIPro.sunSystem().earth());
        assertNull(DI.sunSystem().planet());
        assertNull(DIPro.sunSystem().planet());
    }

}
