package com.github.klee0kai.stone.test.identifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.PhoneComponent;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.identifiers.PhoneOsVersion;
import com.github.klee0kai.test.tech.components.OperationSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MultiIdentifiersTests {
    @Test
    public void differentCreatePhoneTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OperationSystem androidDemo = DI.components().phoneOs(PhoneOsType.Android, null);
        OperationSystem android11 = DI.components().phoneOs(PhoneOsType.Android, new PhoneOsVersion("11"));

        //Then
        assertNotEquals(androidDemo.uuid, android11.uuid);
    }


    @Test
    public void differentMethodDifferentCacheTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OperationSystem android11 = DI.components().phoneOs(PhoneOsType.Android, new PhoneOsVersion("11"));
        OperationSystem androidSimple = DI.components().phoneOs(PhoneOsType.Android);

        //Then
        assertNotEquals(androidSimple.uuid, android11.uuid);
    }

    @Test
    public void cacheByIdentifiersTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OperationSystem android11 = DI.components().phoneOs(PhoneOsType.Android, new PhoneOsVersion("11"));
        OperationSystem android11_2 = DI.components().phoneOs(PhoneOsType.Android, new PhoneOsVersion("11"));

        //Then
        assertEquals(android11.uuid, android11_2.uuid);
    }

    @Test
    public void sepCacheByIdentifiersTest() {
        //Given
        PhoneComponent DI = Stone.createComponent(PhoneComponent.class);

        //When
        OperationSystem android11 = DI.components().phoneOs(PhoneOsType.Android, new PhoneOsVersion("11"));
        OperationSystem android12 = DI.components().phoneOs(PhoneOsType.Android, new PhoneOsVersion("12"));

        //Then
        assertNotEquals(android11.uuid, android12.uuid);
    }
}
