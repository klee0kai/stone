package com.github.klee0kai.stone.test.interfaceprovide;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.earthmirror.EarthComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EarthWayTest {


    @Test
    public void nothingInterfaceTest() {
        //When
        EarthComponent di = Stone.createComponent(EarthComponent.class);

        //Then
        assertNull(di.east().mountain());
        assertNull(di.east().river());
    }

    @Test
    public void nothingAbstractTest() {
        //When
        EarthComponent di = Stone.createComponent(EarthComponent.class);

        //Then
        assertNull(di.west().mountain());
        assertNull(di.west().river());
    }


    @Test
    public void ChristopherColumbusTest() {
        //When
        EarthComponent di = Stone.createComponent(EarthComponent.class);

        //Then
        assertNotNull(di.west().mountainImp());
        assertNotNull(di.west().riverImpl());
    }
}
