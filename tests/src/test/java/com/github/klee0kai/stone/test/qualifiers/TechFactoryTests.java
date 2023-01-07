package com.github.klee0kai.stone.test.qualifiers;

import com.github.klee0kai.stone.Stone;
import com.github.klee0kai.test.di.base_phone.qualifiers.PhoneOsType;
import com.github.klee0kai.test.di.base_phone.qualifiers.RamSize;
import com.github.klee0kai.test.di.phone_factory.TechFactoryComponent;
import com.github.klee0kai.test.tech.components.OperationSystem;
import com.github.klee0kai.test.tech.components.Ram;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TechFactoryTests {

    @Test
    public void differentCreateTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        Ram ram8Gb = DI.factory().ram(new RamSize("8GB"));
        Ram ram8Gb2 = DI.factory().ram(new RamSize("8GB"));

        //Then: created components are different
        assertNotEquals(ram8Gb.uuid, ram8Gb2.uuid);
    }


    @Test
    public void enumDifferentTest() {
        //Given
        TechFactoryComponent DI = Stone.createComponent(TechFactoryComponent.class);

        //When
        OperationSystem android = DI.factory().phoneOs(PhoneOsType.Android);
        OperationSystem android2 = DI.factory().phoneOs(PhoneOsType.Android);
        OperationSystem osNull = DI.factory().phoneOs(null);

        //Then: created components are different
        assertNotEquals(android.uuid, osNull.uuid);
        assertNotEquals(android.uuid, android2.uuid);
    }


}
