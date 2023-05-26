package com.github.klee0kai.stone.test.interfaceprovide;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.earthmirror.EarthComponent;
import com.github.klee0kai.test.mowgli.earth.Cave;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeepCaveTest {


    @Test
    public void justCaveTest() {
        //When
        EarthComponent di = Stone.createComponent(EarthComponent.class);

        //Then: Cave simple constructor is available
        assertNotNull(di.east().cave());
        assertNotNull(di.west().cave());
    }

    @Test
    public void theCaveTest() {
        //When
        EarthComponent di = Stone.createComponent(EarthComponent.class);

        //Then: Cave 2 params constructor is available
        assertNotNull(di.east().cave(Cave.CaveType.Glacier, 2));
        assertNotNull(di.west().cave(Cave.CaveType.Fracture, 6));
    }


}
