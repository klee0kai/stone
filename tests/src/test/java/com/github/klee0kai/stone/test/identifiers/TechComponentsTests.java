package com.github.klee0kai.stone.test.identifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.RamSize;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TechComponentsTests {

    @Test
    public void differentCreateTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        Ram ram8Gb = DI.components().ram(new RamSize("8GB"));
        Ram ram9Gb = DI.components().ram(new RamSize("9GB"));

        //Then: created components are different
        assertNotEquals(ram8Gb.uuid, ram9Gb.uuid);
    }

    @Test
    public void singleCacheTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        Ram ram1 = DI.components().ram(new RamSize("8GB"));
        Ram ram2 = DI.components().ram(new RamSize("8GB"));

        //Then: cached ram
        assertEquals(ram1.uuid, ram2.uuid);
    }

    @Test
    public void enumDifferentTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OperationSystem android = DI.components().phoneOs(PhoneOsType.Android);
        OperationSystem osNull = DI.components().phoneOs(null);
        OperationSystem ios = DI.components().phoneOs(PhoneOsType.Ios);

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid);
        assertNotEquals(android.uuid, ios.uuid);
    }

    @Test
    public void enumCachedTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OperationSystem android = DI.components().phoneOs(PhoneOsType.Android);
        OperationSystem android2 = DI.components().phoneOs(PhoneOsType.Android);

        //Then: created components are different
        assertEquals(android.uuid, android2.uuid);
    }


}
