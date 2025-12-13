package com.github.klee0kai.stone.test.interfaceprovide;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.earthmirror.EarthComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EarthWayTest {

    @Test
    public void ChristopherColumbusTest() {
        //When
        EarthComponent di = Stone.createComponent(EarthComponent.class);

        //Then
        assertNotNull(di.west().mountainImp());
        assertNotNull(di.west().riverImpl());
    }
}
